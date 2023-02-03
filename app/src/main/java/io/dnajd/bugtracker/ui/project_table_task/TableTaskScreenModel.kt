package io.dnajd.bugtracker.ui.project_table_task

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table.interactor.GetProjectTable
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.interactor.GetTableTask
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TableTaskStateScreenModel(
    context: Context,
    taskId: Long,

    private val getTableTask: GetTableTask = Injekt.get(),
    private val getProjectTable: GetProjectTable = Injekt.get(),
) : BugtrackerStateScreenModel<TableTaskScreenState>(context, TableTaskScreenState.Loading) {
    init {
        requestTaskData(taskId)
    }

    private fun requestTaskData(taskId: Long) {
        coroutineScope.launchIO {
            getTableTask.awaitOne(taskId)?.let { task ->
                getProjectTable.awaitOne(id = task.id, ignoreTasks = true)?.let { table ->
                    mutableState.update {
                        TableTaskScreenState.Success(
                            task = task,
                            parentTable = table,
                        )
                    }
                }
            }
        }
    }

    private fun renameTask(newTitle: String) {
        coroutineScope.launchIO {

        }
    }

    fun showDialog(dialog: TableTaskDialog) {
        when(dialog) {
            is TableTaskDialog.BottomSheet -> {
                coroutineScope.launchIO {
                    val tables = dialog.tables.ifEmpty {
                        getProjectTable.await((mutableState.value as TableTaskScreenState.Success).parentTable.projectId, ignoreTasks = true)
                    }
                    mutableState.update {
                        (mutableState.value as TableTaskScreenState.Success).copy(
                            dialog = dialog.copy(
                                tables = tables,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun dismissDialog() {
        coroutineScope.launch {
            mutableState.update {
                (mutableState.value as TableTaskScreenState.Success).copy(
                    dialog = null,
                )
            }
        }
    }

}

sealed class TableTaskDialog {
    data class BottomSheet(val tables: List<ProjectTable> = emptyList()) : TableTaskDialog()
    //object BottomSheet : TableTaskDialog()
}

sealed class TableTaskScreenState {
    
    @Immutable
    object Loading : TableTaskScreenState()

    @Immutable
    data class Success(
        val task: ProjectTableTask,
        val parentTable: ProjectTable,
        val dialog: TableTaskDialog? = null,
    ): TableTaskScreenState()

}