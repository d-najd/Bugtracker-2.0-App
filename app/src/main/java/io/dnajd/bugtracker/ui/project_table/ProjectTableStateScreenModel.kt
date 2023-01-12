package io.dnajd.bugtracker.ui.project_table

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table.interactor.GetProjectTables
import io.dnajd.domain.project_table.interactor.RenameProjectTable
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectTableScreenModel(
    context: Context,
    projectId: Long,

    private val getProjectTables: GetProjectTables = Injekt.get(),
    private val renameProjectTable: RenameProjectTable = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectTableScreenState>(context, ProjectTableScreenState.Loading) {

    init {
        requestTables(projectId)
    }

    private fun requestTables(projectId: Long) {
        coroutineScope.launchIO {
            val projectTables = getProjectTables.await(projectId)
            mutableState.update {
                ProjectTableScreenState.Success(
                    projectTables = projectTables.sortedBy { it.position },
                )
            }
        }
    }

    fun renameTable(tableId: Long, newName: String) {
        coroutineScope.launchIO {
            val state = (mutableState.value as ProjectTableScreenState.Success)
            state.projectTables.find { table -> table.id == tableId }!!.let { originalTable ->
                if(renameProjectTable.await(tableId, newName)) {
                    mutableState.update {
                        val projectTables = state.projectTables.toMutableList()
                        projectTables.remove(originalTable)
                        projectTables.add(originalTable.copy(title = newName))
                        ProjectTableScreenState.Success(
                            projectTables = projectTables
                        )
                    }
                }
            }
        }
    }

    /**
     * switches the dropdown menu to the selected index, if the given index matches the one that is
     * already stored then the index will be set to -1 instead
     */
    fun switchDropdownMenu(index: Int) {
        coroutineScope.launchUI {
            val state = (mutableState.value as ProjectTableScreenState.Success)
            mutableState.update {
                state.copy(
                    dropdownDialogIndex = if(state.dropdownDialogIndex == index) -1 else index
                )
            }
        }
    }
}

sealed class ProjectTableScreenState {
    
    @Immutable
    object Loading : ProjectTableScreenState()

    @Immutable
    data class Success(
        val projectTables: List<ProjectTable>,
        val dropdownDialogIndex: Int = -1,
    ): ProjectTableScreenState()

}