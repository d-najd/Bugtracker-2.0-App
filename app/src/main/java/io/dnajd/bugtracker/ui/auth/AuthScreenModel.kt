package io.dnajd.bugtracker.ui.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.domain.google_auth.service.GoogleAuthApiService
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class AuthScreenModel(
	private val googleAuthApiService: GoogleAuthApiService = Injekt.get(),
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get(),
) : StateScreenModel<AuthScreenState>(AuthScreenState.Success) {
	private val _events: MutableSharedFlow<AuthEvent> = MutableSharedFlow()
	val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	fun signUp(
		googleOAuthToken: String,
		userInfo: CreateUser,
	) = mutex.launchIONoQueue(coroutineScope) {
		val tokenHolder = googleAuthApiService
			.googleSignUp(
				googleOAuthToken,
				userInfo,
			)
			.onFailureWithStackTrace {
				_events.emit(AuthEvent.UserSignUpFailed)
				return@launchIONoQueue
			}
			.getOrThrow()

		jwtAuthPreferenceStore
			.storeTokenHolder(tokenHolder)
			.onFailureWithStackTrace {
				throw IllegalStateException("This should be impossible")
			}

		_events.emit(AuthEvent.UserLoggedIn)
	}

	fun signIn(googleOAuthToken: String) = mutex.launchIONoQueue(coroutineScope) {
		val tokenHolder = googleAuthApiService
			.googleSignIn(googleOAuthToken)
			.onFailureWithStackTrace {
				_events.emit(AuthEvent.UserSignInFailed)
				return@launchIONoQueue
			}
			.getOrThrow()

		jwtAuthPreferenceStore
			.storeTokenHolder(tokenHolder)
			.onFailureWithStackTrace {
				throw IllegalStateException("This should be impossible")
			}

		_events.emit(AuthEvent.UserLoggedIn)
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
