package io.dnajd.domain.utils

/**
 * Same as [Result.onFailure] but also prints stack trace instead of requiring to do so manually
 */
inline fun <T> Result<T>.onFailureWithStackTrace(action: (exception: Throwable) -> Unit): Result<T> {
	return this.onFailure {
		it.printStackTrace()
		action(it)
	}
}