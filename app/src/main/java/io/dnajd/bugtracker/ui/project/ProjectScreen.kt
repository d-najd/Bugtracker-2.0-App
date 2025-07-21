package io.dnajd.bugtracker.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreen
import io.dnajd.bugtracker.ui.util.ScreenFixed
import io.dnajd.data.project.repository.ProjectRepository
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project.ProjectScreenContent
import io.dnajd.presentation.project.dialogs.CreateProjectDialog
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class ProjectScreen : ScreenFixed() {
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { ProjectScreenModel() }

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is ProjectEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()

		if (state is ProjectScreenState.Loading) {
			LoadingScreen()
			return
		}

		val successState = state as ProjectScreenState.Success

		ProjectScreenContent(
			state = successState,
			onProjectClicked = { id ->
				val project = ProjectRepository
					.dataKeys()
					.find { it.id == id }!!

				navigator.push(ProjectTableScreen(project.id))
			},
			onCreateProjectClicked = { screenModel.showDialog(ProjectDialog.CreateProject()) },
			onFilterByNameClicked = { },
		)

		when (successState.dialog) {
			null -> {}
			is ProjectDialog.CreateProject -> {
				CreateProjectDialog(
					onDismissRequest = screenModel::dismissDialog,
					onCreateProjectClicked = screenModel::createProject
				)
			}
		}
	}
}