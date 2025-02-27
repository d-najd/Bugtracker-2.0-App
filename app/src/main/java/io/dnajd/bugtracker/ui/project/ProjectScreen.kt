package io.dnajd.bugtracker.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreen
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project.ProjectScreenContent
import io.dnajd.presentation.project.dialogs.CreateProjectDialog

object ProjectScreen : Screen {
	private fun readResolve(): Any = ProjectScreen

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { ProjectScreenModel(context) }

		val state by screenModel.state.collectAsState()

		if (state is ProjectScreenState.Loading) {
			LoadingScreen()
			return
		}

		val successState = state as ProjectScreenState.Success

		ProjectScreenContent(
			state = successState,
			onProjectClicked = {
				val project = successState.projects.find { project -> project.id == it }!!

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

		/*
		LaunchedEffect(Unit) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is LibraryEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}
		 */
	}
}