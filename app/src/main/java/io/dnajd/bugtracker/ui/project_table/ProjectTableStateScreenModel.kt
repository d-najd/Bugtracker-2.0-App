package io.dnajd.bugtracker.ui.project_table

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project_table.interactor.GetProjectTables
import io.dnajd.domain.project_table.interactor.RenameProjectTable
import io.dnajd.domain.project_table.interactor.SwapProjectTablePositions
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.interactor.SwapProjectTableTaskPositions
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectTableScreenModel(
    context: Context,
    projectId: Long,

    private val getProjectTables: GetProjectTables = Injekt.get(),
    private val renameProjectTable: RenameProjectTable = Injekt.get(),
    private val swapProjectTablePositions: SwapProjectTablePositions = Injekt.get(),
    private val swapProjectTableTaskPositions: SwapProjectTableTaskPositions = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectTableScreenState>(context, ProjectTableScreenState.Loading) {
    private val _events: Channel<ProjectTableEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<ProjectTableEvent> = _events.receiveAsFlow()

    init {
        requestTables(projectId)
    }

    private fun requestTables(projectId: Long) {
        coroutineScope.launchIO {
            val projectTables = getProjectTables.await(projectId)
            mutableState.update {
                ProjectTableScreenState.Success(
                    projectTables = projectTables,
                    events = events,
                )
            }
        }
    }

    fun renameTable(id: Long, newName: String) {
        coroutineScope.launchIO {
            val state = (mutableState.value as ProjectTableScreenState.Success)
            state.projectTables.find { table -> table.id == id }!!.let { originalTable ->
                if(renameProjectTable.await(id, newName)) {
                    // doing it this way so that state changes get updated for sure
                    mutableState.update {
                        val projectTables = state.projectTables.toMutableList()
                        projectTables.remove(originalTable)
                        projectTables.add(originalTable.copy(title = newName))
                        state.copy(
                            projectTables = projectTables
                        )
                    }
                }
            }
        }
    }

    /**
     * swaps the positions of 2 tables
     * @param fId id of the first table
     * @param sId id of the second table
     */
    fun swapTablePositions(fId: Long, sId: Long) {
        coroutineScope.launchIO {
            val state = (mutableState.value as ProjectTableScreenState.Success)
            state.projectTables.find { table -> table.id == sId }!!.let { fTable ->
                state.projectTables.find { table -> table.id == fId }!!.let { sTable ->
                    if(swapProjectTablePositions.await(fId, sId)) {
                        // doing it this way so that state changes get updated for sure
                        mutableState.update {
                            val projectTables = state.projectTables.toMutableList()
                            projectTables.remove(fTable)
                            projectTables.remove(sTable)
                            projectTables.add(fTable.copy(
                                position = sTable.position
                            ))
                            projectTables.add(sTable.copy(
                                position = fTable.position
                            ))
                            state.copy(
                                projectTables = projectTables,
                            )
                        }
                    }
                }
            }
        }
    }


    /**
     * swaps the positions of 2 table tasks.
     *
     * swapTasksTableId is used because with the current implementation the position of the items
     * gets altered when item is moved and doing projectTables state change wont update the items
     * @param fId id of the first table task
     * @param sId id of the second table task
     * @param tableId id of the table in which the tasks are located at, this is used mainly to save
     * the need to compute the table id manually
     */
    fun swapTableTaskPositions(tableId: Long, fId: Long, sId: Long) {
        coroutineScope.launchIO {
            val state = (mutableState.value as ProjectTableScreenState.Success)
            state.projectTables.find { table -> table.id == tableId }!!.let { table ->
                table.tasks.find { task -> task.id == fId }!!.let { fTask ->
                    table.tasks.find { task -> task.id == sId }!!.let { sTask ->
                        if(swapProjectTableTaskPositions.await(fId, sId)) {
                            val projectTables = state.projectTables.toMutableList()
                            projectTables.remove(table)
                            val tasks = table.tasks.toMutableList()
                            tasks.remove(fTask)
                            tasks.remove(sTask)
                            tasks.add(fTask.copy(
                                position = sTask.position,
                            ))
                            tasks.add(sTask.copy(
                                position = fTask.position,
                            ))
                            projectTables.add(table.copy(
                                tasks = tasks,
                            ))
                            mutableState.update {
                                state.copy(
                                    projectTables = projectTables,
                                )
                            }
                        }
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


sealed class ProjectTableEvent {
    data class TasksSwapped(val tableId: Long): ProjectTableEvent()
}

sealed class ProjectTableScreenState {
    
    @Immutable
    object Loading : ProjectTableScreenState()

    /**
     * TODO find a way to get rid of taskSwapped
     */
    @Immutable
    data class Success(
        val projectTables: List<ProjectTable>,
        val dropdownDialogIndex: Int = -1,
        val events: Flow<ProjectTableEvent>
    ): ProjectTableScreenState()

}