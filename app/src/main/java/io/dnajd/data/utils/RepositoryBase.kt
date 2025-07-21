package io.dnajd.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Sets are recommended here since ordering of items should be irrelevant and sets not allowing
 * duplicates
 */
open class RepositoryBase<T1, T2, S>(initialState: S) where S : RepositoryBase.State<T1, T2> {
	protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
	protected val state: StateFlow<S> = mutableState.asStateFlow()

	@Composable
	open fun dataKeysCollected(): Set<T1> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data.keys
		}
	}

	open fun dataKeys(): Set<T1> = state.value.data.keys

	open fun data(): Map<T1, T2> = state.value.data

	open class State<T1, T2>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<T1, T2> = emptyMap(),
	)
}