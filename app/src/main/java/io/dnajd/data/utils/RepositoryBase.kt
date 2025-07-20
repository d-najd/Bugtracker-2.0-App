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
open class RepositoryBase<T, S>(initialState: S) where S : RepositoryBase.State<T> {
	protected val mutableState: MutableStateFlow<S> = MutableStateFlow(initialState)
	protected val state: StateFlow<S> = mutableState.asStateFlow()

	@Composable
	open fun dataCollected(): Set<T> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data.keys
		}
	}

	open fun data(): Set<T> = state.value.data.keys

	open class State<T>(
		/**
		 * The key is the thing being stored and the value is for keeping track of
		 * the last time it was updated
		 */
		open val data: Map<T, Any?> = emptyMap(),
	)
}