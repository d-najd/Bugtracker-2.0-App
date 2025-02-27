package io.dnajd.bugtracker.ui.project_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.project_details.ProjectDetailsScreen
import io.dnajd.bugtracker.ui.user_management.UserManagementScreen
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.bugtracker.ui.util.getScreen
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_settings.ProjectSettingsScreenContent
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class ProjectSettingsScreen(
	private val projectId: Long,
) : Screen {
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		// val router = LocalRouter.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { ProjectSettingsScreenModel(projectId) }

		LaunchedEffect(Unit) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is ProjectSettingsEvent.FailedToRetrieveProjectData -> {
						navigator.pop()
						context.toast(event.stringRes)
					}

					is ProjectSettingsEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()

		if (state is ProjectSettingsScreenState.Loading) {
			LoadingScreen()
			return
		}

		ProjectSettingsScreenContent(
			state = state,
			onBackClicked = navigator::pop,
			onSwitchScreenTabClicked = {
				if (it != ProjectTableSelectedTab.SETTINGS) {
					navigator.replace(it.getScreen(projectId))
				}
			},
			onProjectDetailsClicked = { navigator.push(ProjectDetailsScreen(projectId)) },
			onUserManagementClicked = { navigator.push(UserManagementScreen(projectId)) },
		)
	}
}