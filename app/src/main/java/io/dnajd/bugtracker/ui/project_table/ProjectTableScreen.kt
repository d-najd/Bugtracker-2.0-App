package io.dnajd.bugtracker.ui.project_table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.base.controller.pushController
import io.dnajd.bugtracker.ui.project_table_task.TableTaskController
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table.ProjectTableScreenContent
import io.dnajd.presentation.project_table.dialogs.CreateProjectTableDialog
import io.dnajd.presentation.project_table.dialogs.RenameProjectTableDialog
import io.dnajd.presentation.util.LocalRouter

class ProjectTableScreen(
    private val project: Project,
) : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectTableScreenModel(context, project) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectTableScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as ProjectTableScreenState.Success

        ProjectTableScreenContent(
            state = successState,
            onBackClicked = router::popCurrentController,
            onTableRename = { id, title -> screenModel.showDialog(ProjectTableDialog.RenameTable(id = id, title = title)) },
            onMoveTableTasks = screenModel::moveTableTasks,
            onDeleteTableClicked = screenModel::deleteTable,
            onCreateTableClicked = { screenModel.showDialog(ProjectTableDialog.CreateTable()) },
            onCreateTableTaskMenuClicked = screenModel::showCreateTaskMenu,
            onCreateTableTaskClicked = screenModel::createTask,
            onTaskClicked = { router.pushController(TableTaskController(it)) },
            onSwapTablePositionsClicked = screenModel::swapTablePositions,
            onSwitchDropdownMenuClicked = screenModel::switchDropdownMenu,
        )

        when(val dialog = successState.dialog) {
            null -> {}
            is ProjectTableDialog.CreateTable -> {
                CreateProjectTableDialog(
                    state = successState,
                    placeholderTitle = dialog.title,
                    onDismissRequest = screenModel::dismissDialog,
                    onCreateTableClicked = {
                        screenModel.createTable(table = it)
                        screenModel.dismissDialog()
                    }
                )
            }
            is ProjectTableDialog.RenameTable -> {
                RenameProjectTableDialog(
                    originalTitle = dialog.title,
                    onDismissRequest = screenModel::dismissDialog,
                    onRenameProjectTableClicked = {
                        screenModel.renameTable(id = dialog.id, newName = it)
                        screenModel.dismissDialog()
                    }
                )
            }
        }
    }
}