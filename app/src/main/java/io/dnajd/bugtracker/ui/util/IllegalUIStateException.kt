import kotlin.reflect.KClass

class IllegalUIStateException : IllegalStateException {
	constructor(
		actualState: B,
		expectedStateClass: KClass<out T>,
	) : super(
		"Must be in state ${expectedStateClass.simpleName}, but current state is ${actualState::class.simpleName}"
	)

	constructor() : super()
	constructor(message: String) : super(message)
	constructor(message: String, cause: Throwable) : super(message, cause)
	constructor(cause: Throwable) : super(cause)
}