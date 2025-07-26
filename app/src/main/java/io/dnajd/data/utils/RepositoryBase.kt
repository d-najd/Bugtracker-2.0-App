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

	/**
	 * can't find a way to acquire no-args constructor so I must do this
	 */
	protected abstract fun defaultCacheValue(): V

	/**
	 * Check should be done on init to check whether [K] has member id because this will fail on call if it is
	 * not overridden and there is no id field
	 */
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

	/**
	 * @see combineForUpdate((Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, Pair<K, V>...)
	 */
	internal fun combineForUpdate(vararg newData: K): Map<K, V> {
		return combineForUpdate(
			cacheValue = defaultCacheValue(),
			predicate = defaultCompareForUpdatePredicate(),
			newData = newData,
		)
	}

	/**
	 * @param cacheValue this value will be assigned to all values as current time of last fetch
	 * @see combineForUpdate((Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean, Pair<K, V>...)
	 */
	internal fun combineForUpdate(
		cacheValue: V = defaultCacheValue(),
		predicate: (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean = defaultCompareForUpdatePredicate(),
		vararg newData: K,
	): Map<K, V> {
		return combineForUpdate(
			predicate = predicate,
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
			predicate = defaultCompareForUpdatePredicate(),
			newDataEntries = newDataEntries,
		)
	}

	/**
	 * combines the old data with the new data, meant to be used after data is fetched
	 * @param predicate values matching this predicate will be filtered from the old data, use this
	 * to remove values which already exist in [newDataEntries], works same as [Iterable.none], default
	 * implementation will try to compare id field if it exists using reflection or throw an
	 * exception otherwise
	 * @param newDataEntries the data that will be combined with the old data
	 */
	internal fun combineForUpdate(
		predicate: (Map.Entry<K, V>, Map.Entry<K, V>) -> Boolean = defaultCompareForUpdatePredicate(),
		vararg newDataEntries: Pair<K, V>,
	): Map<K, V> {
		val oldDataEntries = data()
		val newDataEntriesFormatted = newDataEntries.toMap()

		return oldDataEntries
			.filter { oldEntry ->
				newDataEntriesFormatted.none { newEntry ->
					predicate(
						oldEntry,
						newEntry,
					)
				}
			}
			.plus(newDataEntriesFormatted)
	}

	open class State<K, V>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<K, V> = emptyMap(),
	) where V : Date
}
