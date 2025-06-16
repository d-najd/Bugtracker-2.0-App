package io.dnajd.bugtracker.ui.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.domain.google_auth.service.GoogleAuthRepository
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class AuthScreenModel(
	private val googleAuthRepository: GoogleAuthRepository = Injekt.get(),
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get(),
) : StateScreenModel<AuthScreenState>(AuthScreenState.Success) {
	private val _events: Channel<AuthEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<AuthEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	fun signUp(
		googleOAuthToken: String,
		userInfo: CreateUser,
	) = mutex.launchIONoQueue(coroutineScope) {
		val tokenHolder = googleAuthRepository
			.googleSignUp(
				googleOAuthToken, userInfo
			)
			.onFailureWithStackTrace {
				_events.send(AuthEvent.UserSignUpFailed)
				return@launchIONoQueue
			}
			.getOrThrow()

		jwtAuthPreferenceStore
			.storeTokenHolder(tokenHolder)
			.onFailureWithStackTrace {
				throw IllegalStateException("This should be impossible")
			}

		_events.send(AuthEvent.UserLoggedIn)
	}

	fun signIn(googleOAuthToken: String) = mutex.launchIONoQueue(coroutineScope) {
		val tokenHolder = googleAuthRepository
			.googleSignIn(googleOAuthToken)
			.onFailureWithStackTrace {
				_events.send(AuthEvent.UserSignInFailed)
				return@launchIONoQueue
			}
			.getOrThrow()

		jwtAuthPreferenceStore
			.storeTokenHolder(tokenHolder)
			.onFailureWithStackTrace {
				throw IllegalStateException("This should be impossible")
			}

		_events.send(AuthEvent.UserLoggedIn)
	}
}

sealed class AuthScreenState {
	@Immutable data object Success : AuthScreenState()
}

sealed class AuthEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : AuthEvent()

	data object UserSignUpFailed : LocalizedMessage(R.string.error_failed_sign_up)
	data object UserSignInFailed : LocalizedMessage(R.string.error_failed_sign_in)

	data object UserLoggedIn : AuthEvent()
}
