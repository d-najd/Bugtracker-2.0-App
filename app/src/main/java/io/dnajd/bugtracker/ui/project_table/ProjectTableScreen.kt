package io.dnajd.bugtracker.ui.project_table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.bugtracker.ui.base.ProjectTableSelectedTab
import io.dnajd.bugtracker.ui.base.getScreen
import io.dnajd.bugtracker.ui.table_task.TableTaskScreen
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.project_table.ProjectTableScreenContent
import io.dnajd.presentation.project_table.dialogs.CreateProjectTableDialog
import io.dnajd.presentation.project_table.dialogs.RenameProjectTableDialog

class ProjectTableScreen(
    private val project: Project,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectTableScreenModel(context, project) }

        val state by screenModel.state.collectAsState()

        ProjectTableScreenContent(
            state = state,
            onBackClicked = navigator::pop,
            onTableRename = { id, title ->
                screenModel.showDialog(
                    ProjectTableDialog.RenameTable(
                        id = id,
                        title = title
                    )
                )
            },
            onMoveTableTasks = screenModel::moveTableTasks,
            onDeleteTableClicked = screenModel::deleteTable,
            onCreateTableClicked = { screenModel.showDialog(ProjectTableDialog.CreateTable()) },
            onCreateTableTaskMenuClicked = screenModel::showCreateTaskMenu,
            onCreateTableTaskClicked = screenModel::createTask,
            onTaskClicked = { navigator.push(TableTaskScreen(it)) },
            onSwapTablePositionsClicked = screenModel::swapTablePositions,
            onSwitchDropdownMenuClicked = screenModel::switchDropdownMenu,
            onSwitchScreenTabClicked = {
                if (it != ProjectTableSelectedTab.BOARD) {
                    navigator.replace(it.getScreen(project))
                }
            },
        )

        if (state is ProjectTableScreenState.Success) {
            val successState = state as ProjectTableScreenState.Success
            when (val dialog = successState.dialog) {
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
}