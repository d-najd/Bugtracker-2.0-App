package io.dnajd.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.domain.BaseApiEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.reflect.KProperty
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.starProjectedType

/**
 * - If [K] does not have field id then [defaultCompareForUpdatePredicate] must be overridden
 * - [KRT] means "Key Return Type"
 * - If [V] is subclass of [Date] then [defaultCacheValue] must be overridden
 * - [S] means state
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
abstract class RepositoryBase<K, KRT, V, S>(initialState: S) where K : BaseApiEntity<KRT>, V : Date, S : RepositoryBase.State<K, V> {
	protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
	val state: StateFlow<S> = mutableState.asStateFlow()

	open class State<K, V>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<K, V> = emptyMap(),
	) where V : Date

	@Composable
	fun dataKeysCollected(): Set<K> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data.keys
		}
	}

	@Composable
	fun dataKeysCollectedById(vararg ids: KRT): Set<K> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			ids,
		) {
			stateCollected.data.keys
				.filter { ids.contains(it.getId()) }
				.toSet()
		}
	}

	fun dataKeys(): Set<K> = state.value.data.keys

	fun dataKeysById(vararg ids: KRT): Set<K> = dataKeys()
		.filter { ids.contains(it.getId()) }
		.toSet()

	fun data(): Map<K, V> = state.value.data

	/**
	 * The base implementation will remove only instances matching id in [RepositoryBase.State.data], if you
	 * want more complex behaviour like removing from sub-repositories or removing more data override this
	 *
	 * The reason why this method is used instead of some implementation of update is because doing so will
	 * increase the chance of memory leaks [RepositoryBase.State] may have more fields to subclass in future
	 * and even if not it will be harder to maintain multiple implementations or even worse data that should
	 * be removed being shown or not fetching due to not being removed when it should be.
	 *
	 * [T] instances is recommended to always be [KRT] and not a subclass, this would have been the case here
	 * but since kotlin doesn't support overriding generic vararg arguments and this is a workaround for that
	 * behaviour
	 */
	open fun <T : KRT> delete(
		vararg dataById: T,
	) {
		val newData = state.value.data.filterKeys {
			!dataById.contains(it.getId())
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
			.filterNot { oldEntry ->
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
			f.key.getId() == s.key.getId()
		}
	}

	/**
	 * invokes copy method on data object using reflection
	 */
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
}
