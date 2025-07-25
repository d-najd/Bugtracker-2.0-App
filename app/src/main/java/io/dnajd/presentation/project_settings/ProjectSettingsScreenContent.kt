package io.dnajd.presentation.project_settings

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_settings.components.ProjectSettingsContent
import io.dnajd.presentation.project_settings.components.ProjectSettingsTopAppBar

@Composable
fun ProjectSettingsScreenContent(
	state: ProjectSettingsScreenState,
	onBackClicked: () -> Unit,

	onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
	onProjectDetailsClicked: () -> Unit,
	onUserManagementClicked: () -> Unit,
) {
	Scaffold(
		topBar = {
			ProjectSettingsTopAppBar(
				state = state,
				onBackClicked = onBackClicked,
				onSwitchScreenTabClicked = onSwitchScreenTabClicked,
			)
		},
	) { contentPadding ->
		BackHandler { onBackClicked() }

		if (state is ProjectSettingsScreenState.Loading) {
			LoadingScreen()
			return@Scaffold
		}

		val successState = state as ProjectSettingsScreenState.Success
		ProjectSettingsContent(
			state = successState,
			contentPadding = contentPadding,
			onProjectDetailsClicked = onProjectDetailsClicked,
			onUserManagementClicked = onUserManagementClicked,
		)
	}
}
