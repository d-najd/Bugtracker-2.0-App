package io.dnajd.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.starProjectedType

/**
 * - If [K] does not have field id then [defaultCompareForUpdatePredicate] must be overridden
 * - If [V] is subclass of [Date] then [defaultCacheValue] must be overridden
 *
 * TODO add init checks for the cases above so it fails when the class is compiled
 *
 * Recommendations and reasons why stuff is like it is
 *
 * - for fetching data use fetch*IfStale(...) for fetching data, it should return the data that is fetched or
 * if not stale return the data that would have been returned by the fetch, this should also return the just
 * the key (not the cache value)
 *
 * - create method update(data: Map<K, V>) for updating the data, this method should override the current states
 * data, if there is need also add other parameters to update(...) method, and this is the main reason why
 * update method is not defined here since it would be too generic for all use cases
 *
 * - [Set] are preferred over [List] because [List] can contain duplicates and [List] preserves ordering and
 * ordering should not be used here.
 *
 * - [V] is instance of [Date] because in most cases repositories will begin using [Date], and maybe
 * begin using another value later down the line. In this case we will have default implementation
 * for base [Date] and we can check whether [V] is [Date] itself and if it is use the original
 * implementation till we transition fully (if at all).
 */
abstract class RepositoryBase<K, V, S>(initialState: S) where S : RepositoryBase.State<K, V>, V : Date {
	protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
	val state: StateFlow<S> = mutableState.asStateFlow()

	@Composable
	open fun dataKeysCollected(): Set<K> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data.keys
		}
	}

	open fun dataKeys(): Set<K> = state.value.data.keys

	open fun data(): Map<K, V> = state.value.data

	open fun delete(
		vararg dataById: Any,
	) {
		val dataIds = dataById.toSet()
		val newData = state.value.data.filterKeys {
			val idMethod = it!!::class.declaredMembers.firstOrNull { it.name == "id" }

			if (idMethod == null) {
				throw NotImplementedError("Unable to determine id using reflection in: ${this::delete.name}. either implement it or use an alternative")
			}

			!dataIds.contains(idMethod.call(it))
		}

		mutableState.value = copyDataObject(
			state.value,
			state.value::data to newData,
		)
	}

	/**
	 * Must be overridden if [V] is not [Date] but rather subclass of it
	 */
	protected open fun defaultCacheValue(): V {
		@Suppress("UNCHECKED_CAST") return Date() as V
	}

	/**
	 * This value is used for comparison, I don't want to override [equals] or force the user to implement an
	 * method to retrieve the id, although that may be safer
	 *
	 * This method is used to determine id from given key, this is done with reflection by checking for a field
	 * id, will throw an exception if there is no such field in such case override the default implementation.
	 *
	 * @see combineForUpdate
	 * @see delete
	 */
	protected open fun defaultRetrieveId(value: K): Any? {
		val fIdMethod = value!!::class.declaredMembers.firstOrNull { it.name == "id" }

		if (fIdMethod == null) {
			throw NotImplementedError("Unable to determine id using reflection in: ${this::defaultRetrieveId.name}. either implement it or use an alternative")
		}

		return fIdMethod.call(value)
	}

	/**
	 * @see combineForUpdate((Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, Pair<K, V>...)
	 */
	internal fun combineForUpdate(vararg newData: K): Map<K, V> {
		return combineForUpdate(
			cacheValue = defaultCacheValue(),
			filterPredicate = defaultCompareForUpdatePredicate(),
			newData = newData,
		)
	}

	/**
	 * @param cacheValue this value will be assigned to all values as current time of last fetch
	 * @see combineForUpdate((Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, Pair<K, V>...)
	 */
	internal fun combineForUpdate(
		cacheValue: V = defaultCacheValue(),
		filterPredicate: (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean = defaultCompareForUpdatePredicate(),
		vararg newData: K,
	): Map<K, V> {
		return combineForUpdate(
			filterPredicate = filterPredicate,
			newDataEntries = newData
				.map {
					Pair(
						it,
						cacheValue,
					)
				}
				.toTypedArray(),
		)
	}

	/**
	 *
	 * @see combineForUpdate((Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, Pair<K, V>...)
	 */
	internal fun combineForUpdate(
		vararg newDataEntries: Pair<K, V>,
	): Map<K, V> {
		return combineForUpdate(
			filterPredicate = defaultCompareForUpdatePredicate(),
			newDataEntries = newDataEntries,
		)
	}

	/**
	 * combines the old data with the new data, meant to be used after data is fetched
	 * @param filterPredicate values not matching this predicate will be filtered from the old data, use this
	 * to remove values which already exist in [newDataEntries], works same as [Iterable.any], default
	 * implementation will try to compare id field if it exists using reflection or throw an
	 * exception otherwise, inspired from [Iterable.filter]
	 * @param newDataEntries the data that will be combined with the old data
	 */
	internal fun combineForUpdate(
		filterPredicate: (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean = defaultCompareForUpdatePredicate(),
		vararg newDataEntries: Pair<K, V>,
	): Map<K, V> {
		val oldDataEntries = data()
		val newDataEntriesFormatted = newDataEntries.toMap()

		return oldDataEntries
			.filter { oldEntry ->
				newDataEntriesFormatted.any { newEntry ->
					filterPredicate(
						oldEntry,
						newEntry,
					)
				}
			}
			.plus(newDataEntriesFormatted)
	}

	private fun defaultCompareForUpdatePredicate(): (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean {
		return { f, s ->

			val fIdMethod = f.key!!::class.declaredMembers.firstOrNull { it.name == "id" }
			val sIdMethod = s.key!!::class.declaredMembers.firstOrNull { it.name == "id" }

			if (fIdMethod == null || sIdMethod == null) {
				throw NotImplementedError("Unable to determine id using reflection in: ${this::defaultCompareForUpdatePredicate.name}. either implement it or use an alternative")
			}

			defaultRetrieveId(f.key) == defaultRetrieveId(s.key)
		}
	}

	private fun <T : Any> copyDataObject(
		toCopy: T,
		vararg properties: Pair<KProperty<*>, Any?>,
	): T {
		val dataClass = toCopy::class
		require(dataClass.isData) { "Type of object to copy must be a data class" }
		val copyFunction = dataClass.memberFunctions.first { it.name == "copy" }
		val parameters = buildMap {
			put(
				copyFunction.instanceParameter!!,
				toCopy,
			)
			properties.forEach { (property, value) ->
				val parameter = requireNotNull(
					copyFunction.parameters.firstOrNull { it.name == property.name },
				) { "Parameter not found for property ${property.name}" }
				value?.let {
					require(
						parameter.type.isSupertypeOf(it::class.starProjectedType),
					) { "Incompatible type of value for property ${property.name}" }
				}
				put(
					parameter,
					value,
				)
			}
		}
		@Suppress("UNCHECKED_CAST") return copyFunction.callBy(parameters) as T
	}

	open class State<K, V>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<K, V> = emptyMap(),
	) where V : Date
}
