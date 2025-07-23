package io.dnajd.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.reflect.full.declaredMembers

/**
 * Sets are recommended here since ordering of items should be irrelevant and sets not allowing
 * duplicates
 *
 * [V] is instance of [Date] because in most cases repositories will begin using [Date], and maybe
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


	protected abstract fun defaultCacheValue(): V

	/**
	 * @see combineForUpdate(V, (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, K...) For full documentation
	 */
	internal fun combineForUpdate(vararg newData: K): Map<K, V> {
		return combineForUpdate(
			defaultCacheValue(),
			newData = newData
		)
	}

	/**
	 * @see combineForUpdate(V, (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, K...) For full documentation
	 */
	internal fun combineForUpdate(
		value: V = defaultCacheValue(),
		vararg newData: K,
	): Map<K, V> {
		return combineForUpdate(
			value,
			newData = newData
		)
	}

	/**
	 * combines the old data with the new data, meant to be used after data is fetched
	 * @param value this value will be assigned to all values as current time of last fetch
	 * @param predicate values matching this predicate will be filtered from the old data, use this
	 * to remove values which already exist in [newData], works same as [Iterable.none], default
	 * implementation will try to compare id field if it exists using reflection or throw an
	 * exception otherwise
	 * @param newData the data that will be combined with the old data
	 */
	internal open fun combineForUpdate(
		value: V = defaultCacheValue(),
		predicate: (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean = defaultCompareForUpdatePredicate(),
		vararg newData: K,
	): Map<K, V> {
		val oldDataEntries = data()
		val newDataEntries = newData
			.toSet()
			.associateWith {
				value
			}

		return oldDataEntries
			.filter { oldEntry ->
				newDataEntries.none { newEntry ->
					predicate(
						oldEntry,
						newEntry
					)
				}
			}
			.plus(newDataEntries)
	}

	protected open fun defaultCompareForUpdatePredicate(): (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean {
		return { f, s ->

			val fIdMethod = f.key!!::class.declaredMembers.firstOrNull { it.name == "id" }
			val sIdMethod = s.key!!::class.declaredMembers.firstOrNull { it.name == "id" }

			if (fIdMethod == null || sIdMethod == null) {
				throw NotImplementedError("Unable to determine id using reflection in: ${this::defaultCompareForUpdatePredicate.name}. either implement it or use an alternative")
			}

			fIdMethod.call(f.key) == sIdMethod.call(s.key)
		}
	}

	open class State<K, V>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<K, V> = emptyMap(),
	) where V : Date
}