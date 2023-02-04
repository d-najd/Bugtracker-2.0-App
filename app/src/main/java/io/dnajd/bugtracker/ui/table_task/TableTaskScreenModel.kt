package io.dnajd.bugtracker.ui.table_task

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table.interactor.GetProjectTable
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.table_task.interactor.GetTableTask
import io.dnajd.domain.table_task.interactor.SwapTableTaskTable
import io.dnajd.domain.table_task.interactor.UpdateTableTaskDescription
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TableTaskStateScreenModel(
    context: Context,
    taskId: Long,

    private val getTableTask: GetTableTask = Injekt.get(),
    private val getProjectTable: GetProjectTable = Injekt.get(),
    private val swapTableTaskTable: SwapTableTaskTable = Injekt.get(),
    private val updateTaskDescription: UpdateTableTaskDescription = Injekt.get(),
) : BugtrackerStateScreenModel<TableTaskScreenState>(context, TableTaskScreenState.Loading) {
    private val _events: Channel<TableTaskEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<TableTaskEvent> = _events.receiveAsFlow()

    init {
        requestTaskData(taskId)
    }

    private fun requestTaskData(taskId: Long) {
        coroutineScope.launchIO {
            getTableTask.awaitOne(taskId)?.let { task ->
                getProjectTable.awaitOne(id = task.tableId, ignoreTasks = true)?.let { table ->
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

    fun updateDescription(newDescription: String) {
        val successState = (mutableState.value as TableTaskScreenState.Success)
        coroutineScope.launchIO {
            if(updateTaskDescription.await(id = successState.task.id, description = newDescription)) {
                mutableState.update {
                    successState.copy(
                        task = successState.task.copy(
                            description = newDescription
                        )
                    )
                }
                dismissSheet()
            } else {
                _events.send(TableTaskEvent.FailedToUpdateDescription)
            }
        }
    }

    fun swapTable(tableId: Long) {
        coroutineScope.launchIO {
            if(swapTableTaskTable.await(id = (mutableState.value as TableTaskScreenState.Success).task.id, tableId = tableId)) {
                val table = getProjectTable.awaitOne(id = tableId, ignoreTasks = true)
                if(table == null) {
                    _events.send(TableTaskEvent.CanNotGetParentTable)
                    return@launchIO
                }
                mutableState.update {
                    (mutableState.value as TableTaskScreenState.Success).copy(
                        task = (mutableState.value as TableTaskScreenState.Success).task.copy(
                            tableId = tableId,
                            position = 0,
                        ),
                        parentTable = table,
                    )
                }
            } else {
                _events.send(TableTaskEvent.FailedToSwapTable)
            }
        }
    }

    fun showSheet(sheet: TableTaskSheet) {
        when(sheet) {
            is TableTaskSheet.BottomSheet -> {
                coroutineScope.launchIO {
                    val tables = sheet.tables.ifEmpty {
                        getProjectTable.await((mutableState.value as TableTaskScreenState.Success).parentTable.projectId, ignoreTasks = true)
                    }
                    mutableState.update {
                        (mutableState.value as TableTaskScreenState.Success).copy(
                            sheet = sheet.copy(
                                tables = tables,
                            ),
                        )
                    }
                }
            }
            else -> {
                mutableState.update {
                    (mutableState.value as TableTaskScreenState.Success).copy(
                        sheet = sheet,
                    )
                }
            }
        }
    }

    fun dismissSheet() {
        coroutineScope.launch {
            mutableState.update {
                (mutableState.value as TableTaskScreenState.Success).copy(
                    sheet = null,
                )
            }
        }
    }
}

sealed class TableTaskSheet {
    data class BottomSheet(val tables: List<ProjectTable> = emptyList()) : TableTaskSheet()
    data class AlterDescriptionSheet(val description: String = ""): TableTaskSheet()
}

sealed class TableTaskEvent {
    sealed class LocalizedMessage(@StringRes val stringRes: Int) : TableTaskEvent()

    object CanNotGetParentTable : LocalizedMessage(R.string.error_invalid_table_id)
    object FailedToSwapTable : LocalizedMessage(R.string.error_table_swap)
    object FailedToUpdateDescription : LocalizedMessage(R.string.error_description_update)
}

sealed class TableTaskScreenState {

    @Immutable
    object Loading : TableTaskScreenState()

    @Immutable
    data class Success(
        val task: TableTask,
        val parentTable: ProjectTable,
        val sheet: TableTaskSheet? = null,
    ): TableTaskScreenState()

}