package io.dnajd.bugtracker.ui.project_table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table.ProjectTableScreenContent
import io.dnajd.presentation.util.LocalRouter

class ProjectTableScreen(
    private val projectId: Long,
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectTableScreenModel(context, projectId) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectTableScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as ProjectTableScreenState.Success

        ProjectTableScreenContent(
            state = successState,
            onBackClicked = router::popCurrentController,
            onTableRename = screenModel::renameTable,
        )
    }
}