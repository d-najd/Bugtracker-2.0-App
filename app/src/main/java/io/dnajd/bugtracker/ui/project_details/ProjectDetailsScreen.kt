package io.dnajd.bugtracker.ui.project_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.asTransaction
import io.dnajd.bugtracker.ui.base.controller.setRoot
import io.dnajd.bugtracker.ui.project.ProjectController
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsController
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState
import io.dnajd.bugtracker.ui.project_table.ProjectTableController
import io.dnajd.bugtracker.ui.util.setAtBackstack
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_details.ProjectDetailsScreenContent
import io.dnajd.presentation.util.LocalRouter
import io.dnajd.util.toast
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

        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                if(event is ProjectDetailsEvent.LocalizedMessage) {
                    context.toast(event.stringRes)
                }
                when (event) {
                    is ProjectDetailsEvent.DeleteProject -> {
                        router.setAtBackstack(0, ProjectController())
                        router.popToRoot()
                    }
                    is ProjectDetailsEvent.InvalidProjectId -> {
                        router.popCurrentController()
                    }
                    is ProjectDetailsEvent.ProjectModified -> {
                        // pain
                        for((index, routerTransaction) in router.backstack.withIndex()){
                            when(routerTransaction.controller){
                                // TODO this does not really seem that expandable, create better
                                //  system possibly using https://stackoverflow.com/questions/71417326/jetpack-compose-topappbar-with-dynamic-actions
                                is ProjectSettingsController -> {
                                    router.setAtBackstack(index, ProjectSettingsController(event.project))
                                }
                                is ProjectTableController -> {
                                    router.setAtBackstack(index, ProjectTableController(event.project))
                                }
                                is ProjectController -> {
                                    router.setAtBackstack(index, ProjectController())
                                }
                                else -> { }
                            }
                        }
                    }
                    is ProjectDetailsEvent.LocalizedMessage -> { }
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
            onBackClicked = router::popCurrentController,
            onRenameProjectClicked = screenModel::renameProject,
            onDeleteProjectClicked = screenModel::deleteProject,
        )
    }
}