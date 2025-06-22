package io.dnajd.bugtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.theme.BugtrackerTheme
import io.dnajd.bugtracker.ui.auth.AuthScreen
import io.dnajd.bugtracker.ui.project.ProjectScreen
import io.dnajd.data.utils.JwtTokenRefresher
import io.dnajd.domain.DomainModule
import io.dnajd.presentation.components.LoadingScreen
import uy.kohesive.injekt.Injekt

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		Injekt.importModule(MainActivityModule(this))
		Injekt.importModule(DomainModule())

		setContent {
			BugtrackerTheme {
				var checkingTokens by remember { mutableStateOf(CheckingTokensState.LOADING) }
				var checkingTokensStarted by rememberSaveable { mutableStateOf(false) }

				// Prevent re-invoking of checking token validity
				LaunchedEffect(Unit) {
					if (!checkingTokensStarted) {
						JwtTokenRefresher.checkTokenValidity(
							noUserSignUpNeeded = {
								checkingTokens = CheckingTokensState.REFRESH_VALID
							},
							userSignUpNeeded = {
								checkingTokens = CheckingTokensState.REFRESH_INVALID
							})
						checkingTokensStarted = true
					}
				}

				when (checkingTokens) {
					CheckingTokensState.LOADING -> {
						LoadingScreen()
					}

					CheckingTokensState.REFRESH_VALID -> {
						Navigator(ProjectScreen)
					}

					CheckingTokensState.REFRESH_INVALID -> {
						Navigator(AuthScreen)
					}
				}                /*
				val projectFake = Project(
					id = 1,
					owner = "user1",
					title = "Fake Title",
				)
				 */


				// router.setRoot(ProjectController())
				// router.setRoot(ProjectTableController(projectFake))
				// router.setRoot(TableTaskController(1L))
				// router.setRoot(ProjectSettingsController(projectFake))
				// router.setRoot(ProjectDetailsController(projectFake))
				// router.setRoot(ProjectUserManagementController(1L))
			}
		}
	}

	enum class CheckingTokensState {
		LOADING, // The tokens are checked
		REFRESH_VALID,
		REFRESH_INVALID,
	}
}