package io.dnajd.bugtracker.ui.project_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_details.ProjectDetailsScreenContent
import io.dnajd.presentation.util.LocalRouter
import kotlinx.coroutines.flow.collectLatest

class ProjectDetailsScreen(
    private val project: Project,
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectDetailsScreenModel(context, project) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectDetailsScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as ProjectDetailsScreenState.Success

        ProjectDetailsScreenContent(
            state = successState,
            onBackClicked = router::popCurrentController,
            onRenameProjectClicked = screenModel::renameProject,
            onDeleteProjectClicked = screenModel::deleteProject,
        )

        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                when (event) {
                    is ProjectDetailsEvent.DeleteProject -> {
                        router.popToRoot()
                    }
                }
            }
        }
    }
}