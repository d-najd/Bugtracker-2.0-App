package io.dnajd.bugtracker.ui.project_table_task

import android.content.Context
import androidx.compose.runtime.Immutable
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.util.BugtrackerStateScreenModel

class TableTaskStateScreenModel(
    context: Context,
    projectId: Long,
    tableId: Long,
    taskId: Long,

    // private val getProjectTables: GetProjectTables = Injekt.get(),
) : BugtrackerStateScreenModel<TableTaskScreenState>(context, TableTaskScreenState.Loading) {

    init {
        // requestTables(projectId)
    }

    /*
    private fun requestTables(projectId: Long) {
        coroutineScope.launchIO {
            val projectTables = getProjectTables.await(projectId).sortedBy { it.position }.map { table ->
                table.copy(
                    tasks = table.tasks.sortedBy { it.position }
                )
            }
            mutableState.update {
                ProjectTableScreenState.Success(
                    projectTables = projectTables,
                )
            }
        }
    }
     */

}

sealed class TableTaskScreenState {
    
    @Immutable
    object Loading : TableTaskScreenState()

    /**
     * TODO find a way to get rid of taskMoved, using events does not work properly
     */
    @Immutable
    data class Success(
        val projectTables: List<ProjectTable>,
    ): TableTaskScreenState()

}