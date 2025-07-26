package io.dnajd.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex

sealed class ExecutionResult<out T> {
	data class Executed<out T>(val result: T) : ExecutionResult<T>()
	object Skipped : ExecutionResult<Nothing>()
}

/**
 * Executes [action] if mutex is immediately available, ignoring concurrent calls.
 * Any exceptions in [action] will propagate to the caller.
 * */
suspend fun <T> Mutex.withLockNoQueue(
	action: suspend () -> T,
): ExecutionResult<T> {
	if (!this.tryLock()) {
		return ExecutionResult.Skipped
	}

	return try {
		ExecutionResult.Executed(action())
	} finally {
		unlock()
	}
}

/**
 * Executes [block] if mutex is immediately available. Otherwise [onSkip] will be invoked.
 * Any exceptions in [block] will propagate to the caller.
 * @param scope needed for invoking [CoroutineScope.launchUI]
 * @param onSkip executed if there is call and the mutex is not immediately available
 * @param block block to be executed
 * @see CoroutineScope.launchUI
 * @see Mutex.withLockNoQueue
 */
fun Mutex.launchUINoQueue(
	scope: CoroutineScope,
	onSkip: () -> Unit = {},
	block: suspend CoroutineScope.() -> Unit,
): Job {
	return scope.launchUI {
		when (withLockNoQueue { block() }) {
			is ExecutionResult.Executed -> {}
			ExecutionResult.Skipped -> onSkip()
		}
	}
}

/**
 * Executes [block] if mutex is immediately available. Otherwise [onSkip] will be invoked.
 * Any exceptions in [block] will propagate to the caller.
 * @param scope needed for invoking [CoroutineScope.launchIO]
 * @param onSkip executed if there is call and the mutex is not immediately available
 * @param block block to be executed
 * @see CoroutineScope.launchIO
 * @see Mutex.withLockNoQueue
 */
fun Mutex.launchIONoQueue(
	scope: CoroutineScope,
	onSkip: () -> Unit = {},
	block: suspend CoroutineScope.() -> Unit,
): Job {
	return scope.launchIO {
		when (withLockNoQueue { block() }) {
			is ExecutionResult.Executed -> {}
			ExecutionResult.Skipped -> onSkip()
		}
	}
}
