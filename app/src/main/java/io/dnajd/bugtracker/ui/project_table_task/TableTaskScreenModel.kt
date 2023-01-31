package io.dnajd.bugtracker.ui.project_table_task

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table.interactor.GetProjectTable
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.interactor.GetProjectTableTask
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

    private val getProjectTableTask: GetProjectTableTask = Injekt.get(),
    private val getProjectTable: GetProjectTable = Injekt.get(),
) : BugtrackerStateScreenModel<TableTaskScreenState>(context, TableTaskScreenState.Loading) {
    init {
        requestTaskData(taskId)
    }

    private fun requestTaskData(taskId: Long) {
        coroutineScope.launchIO {
            getProjectTableTask.awaitOne(taskId)?.let { task ->
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
            is TableTaskDialog.BottomNavDialog -> {
                coroutineScope.launch {
                    mutableState.update {
                        (mutableState.value as TableTaskScreenState.Success).copy(
                            dialog = dialog,
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
    object BottomNavDialog : TableTaskDialog()
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