package io.dnajd.bugtracker.ui.project_table_task

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table_task.interactor.GetProjectTableTasks
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TableTaskStateScreenModel(
    context: Context,
    projectId: Long,
    tableId: Long,
    taskId: Long,

    private val getProjectTableTasks: GetProjectTableTasks = Injekt.get(),
) : BugtrackerStateScreenModel<TableTaskScreenState>(context, TableTaskScreenState.Loading) {

    init {
        requestTaskData(taskId)
    }

    private fun requestTaskData(taskId: Long) {
        coroutineScope.launchIO {
            getProjectTableTasks.awaitOne(taskId)?.let { task ->
                mutableState.update {
                    TableTaskScreenState.Success(
                        task = task
                    )
                }
            }
        }
    }
}

sealed class TableTaskScreenState {
    
    @Immutable
    object Loading : TableTaskScreenState()

    /**
     * TODO find a way to get rid of taskMoved, using events does not work properly
     */
    @Immutable
    data class Success(
        val task: ProjectTableTask,
    ): TableTaskScreenState()

}