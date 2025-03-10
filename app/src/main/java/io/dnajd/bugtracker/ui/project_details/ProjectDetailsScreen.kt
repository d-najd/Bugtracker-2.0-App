package io.dnajd.bugtracker.ui.project_details

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
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_details.ProjectDetailsScreenContent
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

class ProjectDetailsScreen(
	private val projectId: Long,
) : Screen {
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val context = LocalContext.current
		val screenModel = rememberScreenModel { ProjectDetailsScreenModel(projectId) }

		LaunchedEffect(screenModel.events) {
			screenModel.events.collectLatest { event ->
				when (event) {
					is ProjectDetailsEvent.DeleteProject -> {
						navigator.popAll()
						navigator.push(ProjectScreen)
					}

					is ProjectDetailsEvent.FailedToRetrieveProjectData -> {
						navigator.pop()

						context.toast(event.stringRes)
					}

					is ProjectDetailsEvent.LocalizedMessage -> {
						context.toast(event.stringRes)
					}
				}
			}
		}

		val state by screenModel.state.collectAsState()

		if (state is ProjectDetailsScreenState.Loading) {
			LoadingScreen()
			return
		}

		val successState = state as ProjectDetailsScreenState.Success

		ProjectDetailsScreenContent(
			state = successState,
			onBackClicked = navigator::pop,
			onRenameProjectClicked = screenModel::renameProject,
			onDeleteProjectClicked = screenModel::deleteProject,
		)
	}
}