package io.dnajd.bugtracker.ui.util

class IllegalUIStateException : IllegalStateException {
	companion object {
		private fun getMessageOrThrowException(currentState: Any, expectedState: Any): String {
			if (currentState::class != expectedState::class) {
				throw IllegalArgumentException(invalidArgumentsMessage(currentState, expectedState))
			}

			return message(currentState, expectedState)
		}

		private fun invalidArgumentsMessage(currentState: Any, expectedState: Any) =
			"Cannot create ${IllegalUIStateException::class::simpleName}: state types differ " +
					"(${currentState::class::simpleName} vs ${expectedState::class::simpleName})"

		private fun message(currentState: Any, expectedState: Any) =
			"Must be called from state ${currentState::class.simpleName}, " +
					"but was called with state ${expectedState::class.simpleName}"
	}

	constructor() : super()
	constructor(cause: Throwable) : super(cause)

	@Throws(IllegalArgumentException::class)
	constructor(currentState: Any, expectedState: Any) : super(
		getMessageOrThrowException(currentState, expectedState)
	)

	@Throws(IllegalArgumentException::class)
	constructor(currentState: Any, expectedState: Any, cause: Throwable) : super(
		getMessageOrThrowException(currentState, expectedState),
		cause
	)
}