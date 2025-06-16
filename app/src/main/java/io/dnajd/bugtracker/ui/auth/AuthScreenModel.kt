package io.dnajd.bugtracker.ui.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.domain.google_auth.service.GoogleAuthRepository
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class AuthScreenModel(
	val googleAuthRepository: GoogleAuthRepository = Injekt.get(),
) : StateScreenModel<AuthScreenState>(AuthScreenState.Success) {
	private val _events: Channel<AuthEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<AuthEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	fun signUp(
		googleOAuthToken: String,
		username: String,
	) = mutex.launchIONoQueue(coroutineScope) {
		googleAuthRepository.googleSignUp(googleOAuthToken, CreateUser(username))
			.onFailureWithStackTrace { }
	}

	fun signIn(googleOAuthToken: String) = mutex.launchIONoQueue(coroutineScope) {

	}
}

sealed class AuthScreenState {
	@Immutable
	data object Success : AuthScreenState()
}

sealed class AuthEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : AuthEvent()

	data object UserLoggedIn : AuthEvent()
}
