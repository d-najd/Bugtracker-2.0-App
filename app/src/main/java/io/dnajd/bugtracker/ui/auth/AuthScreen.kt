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
					AuthEvent.UserLoggedIn -> {
						navigator.replace(ProjectScreen)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()
		val successState = state as AuthScreenState.Success

		AuthScreenContent(successState)
	}
}