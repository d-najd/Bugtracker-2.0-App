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

	/**
	 * Must be overridden if custom [State] is used
	 * @see State
	 */
	open fun update(
		data: T,
		setDataFetched: Boolean = true,
	) {

		// Check is done on init
		@Suppress("UNCHECKED_CAST")

		mutableState.value = State<T>(
			fetchedData = setDataFetched,
			data = data
		) as S
	}

	/**
	 * [update] must be overridden if this is overridden
	 * @see update
	 */
	open class State<T>(
		val data: T,
		/**
		 * Should be set to true only when all data has been set, fetching individual fields should
		 * not set this to true, if you want finer control consider overriding [State]
		 */
		val fetchedData: Boolean = false,
	)

	init {

		// This fails if the class if [initialState] is not the same as [RepositoryBase.State]
		// even if [S] is [RepositoryBase.State], but there shouldn't be a case when that happens
		val isStateOverridden = initialState::class != State::class

		// Only methods in the new class seem to be included
		val isUpdateOverridden =
			this::class.java.declaredMethods.any { it.name == RepositoryBase<T, S>::update.name }

		if (isStateOverridden && !isUpdateOverridden) {
			throw TypeCastException("When using custom state override ${RepositoryBase<T, S>::update.name} as well")
		}
	}
}