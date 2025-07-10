package io.dnajd.bugtracker.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.project.ProjectScreen
import io.dnajd.presentation.auth.AuthScreenContent
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

object AuthScreen : Screen {
	private fun readResolve(): Any = AuthScreen

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { AuthScreenModel() }

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is AuthEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}

					AuthEvent.UserLoggedIn -> {
						navigator.replaceAll(ProjectScreen())
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()
		val successState = state as AuthScreenState.Success

		AuthScreenContent(
			screenState = successState,
			onSignInClicked = screenModel::signIn,
			onSignUpClicked = screenModel::signUp,
		)
	}
}