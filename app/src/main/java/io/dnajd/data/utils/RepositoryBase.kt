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
	val state: StateFlow<S> = mutableState.asStateFlow()

	@Composable
	fun dataCollected(): T {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.data
		}
	}

	fun data(): T = state.value.data

	open class State<T>(
		open val data: T,
	)
}