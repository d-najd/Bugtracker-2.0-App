package io.dnajd.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

/**
 * Sets are recommended here since ordering of items should be irrelevant and sets not allowing
 * duplicates
 *
 * [V] is instance of [Date] because in most cases repositories will begin using [Date], and maybe
 * begin using another value later down the line. In this case we will have default implementation
 * for base [Date] and we can check whether [V] is [Date] itself and if it is use the original
 * implementation till we transition fully (if at all).
 */
open class RepositoryBase<K, V, S>(initialState: S) where S : RepositoryBase.State<K, V>, V : Date {
	protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
	protected val state: StateFlow<S> = mutableState.asStateFlow()

	@Composable
	open fun dataKeysCollected(): Set<K> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data.keys
		}
	}

	open fun dataKeys(): Set<K> = state.value.data.keys

	open fun data(): Map<K, V> = state.value.data

	open class State<K, V>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<K, V> = emptyMap(),
	) where V : Date
}