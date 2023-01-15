package io.dnajd.bugtracker.ui.project_table_task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table.ProjectTableScreenContent
import io.dnajd.presentation.project_table_task.TableTaskScreenContent
import io.dnajd.presentation.util.LocalRouter

class TableTaskScreen(
    private val projectId: Long,
    private val tableId: Long,
    private val taskId: Long
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { TableTaskStateScreenModel(
            context = context,
            projectId = projectId,
            tableId = tableId,
            taskId = taskId,
        ) }

        val state by screenModel.state.collectAsState()

        if (state is TableTaskScreenState.Loading) {
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