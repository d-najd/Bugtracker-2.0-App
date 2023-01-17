package io.dnajd.bugtracker.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.base.controller.pushController
import io.dnajd.bugtracker.ui.project_table.ProjectTableController
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project.ProjectScreenContent
import io.dnajd.presentation.project.dialogs.CreateProjectDialog
import io.dnajd.presentation.util.LocalRouter

object ProjectScreen : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectScreenModel(context) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectScreenState.Loading){
            LoadingScreen()
            return
        }

        val successState = state as ProjectScreenState.Success

        ProjectScreenContent(
            state = successState,
            onProjectClicked = { router.pushController(ProjectTableController(it)) },
            onCreateProjectClicked = { screenModel.showDialog(ProjectDialog.CreateProject(title = "")) },
            onFilterByNameClicked = { },
        )

        when(successState.dialog) {
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