package io.dnajd.bugtracker.ui.auth

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.preference.service.PreferenceStore
import io.dnajd.util.launchUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class AuthScreenModel(
	preferenceStore: PreferenceStore = Injekt.get(),
) : StateScreenModel<AuthScreenState>(AuthScreenState.Loading) {
	companion object {
		const val TOKEN_ID = "oauth_token_preference"
	}

	private val _events: Channel<AuthEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<AuthEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		coroutineScope.launchUI {
			val token = preferenceStore.getString(TOKEN_ID)

			if (token.isSet()) {
				_events.send(AuthEvent.UserLoggedIn)
			} else {
				mutableState.update {
					AuthScreenState.SignedOut
				}
			}
		}
	}


}

sealed class AuthScreenState {
	@Immutable
	object Loading : AuthScreenState()

	@Immutable
	object SignedOut : AuthScreenState()

	@Immutable
	object PickingUsername : AuthScreenState()
}

sealed class AuthEvent {
	object UserLoggedIn : AuthEvent()
}
