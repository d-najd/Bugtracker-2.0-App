package io.dnajd.bugtracker.ui.project_user_management

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table_task.TableTaskScreenContent
import io.dnajd.presentation.util.LocalRouter

class ProjectUserManagementScreen(
    private val projectId: Long
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectUserManagementScreenModel(context, projectId) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectUserManagementScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as TableTaskScreenState.Success

        TableTaskScreenContent(
            state = successState,
            onBackClicked = router::popCurrentController,
        )
    }
}