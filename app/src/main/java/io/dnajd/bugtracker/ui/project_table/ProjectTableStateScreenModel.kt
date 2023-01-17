package io.dnajd.bugtracker.ui.project_table

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.interactor.CreateProjectTable
import io.dnajd.domain.project_table.interactor.GetProjectTables
import io.dnajd.domain.project_table.interactor.RenameProjectTable
import io.dnajd.domain.project_table.interactor.SwapProjectTables
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.interactor.MoveProjectTableTask
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectTableScreenModel(
    context: Context,
    project: Project,

    private val getTables: GetProjectTables = Injekt.get(),
    private val createTable: CreateProjectTable = Injekt.get(),
    private val renameTable: RenameProjectTable = Injekt.get(),
    private val swapTables: SwapProjectTables = Injekt.get(),
    private val moveTableTask: MoveProjectTableTask = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectTableScreenState>(context, ProjectTableScreenState.Loading) {

    init {
        requestTables(project)
    }

    private fun requestTables(project: Project) {
        coroutineScope.launchIO {
            val projectTables = getTables.await(project.id).sortedBy { it.position }.map { table ->
                table.copy(
                    tasks = table.tasks.sortedBy { it.position }
                )
            }
            mutableState.update {
                ProjectTableScreenState.Success(
                    project = project,
                    tables = projectTables,
                )
            }
        }
    }

    fun renameTable(id: Long, newName: String) {
        val state = (mutableState.value as ProjectTableScreenState.Success)
        coroutineScope.launchIO {
            state.tables.find { table -> table.id == id }!!.let { originalTable ->
                if(renameTable.await(id, newName)) {
                    // doing it this way so that state changes get updated for sure
                    mutableState.update {
                        val projectTables = state.tables.toMutableList()
                        projectTables.remove(originalTable)
                        projectTables.add(originalTable.copy(title = newName))
                        state.copy(
                            tables = projectTables
                        )
                    }
                }
            }
        }
    }

    fun createTable(table: ProjectTable) {
        val state = (mutableState.value as ProjectTableScreenState.Success)
        coroutineScope.launchIO {
            createTable.awaitOne(table)?.let { persistedTable ->
                val tables = state.tables.toMutableList()
                tables.add(persistedTable)
                state.copy(
                    tables = tables
                )
            }
            dismissDialog()
        }
    }

    /**
     * swaps the positions of 2 tables
     * @param fId id of the first table
     * @param sId id of the second table
     */
    fun swapTablePositions(fId: Long, sId: Long) {
        val state = (mutableState.value as ProjectTableScreenState.Success)
        coroutineScope.launchIO {
            state.tables.find { table -> table.id == sId }!!.let { fTable ->
                state.tables.find { table -> table.id == fId }!!.let { sTable ->
                    if(swapTables.await(fId, sId)) {
                        // doing it this way so that state changes get updated for sure
                        mutableState.update {
                            val projectTables = state.tables.toMutableList()
                            projectTables.remove(fTable)
                            projectTables.remove(sTable)
                            projectTables.add(fTable.copy(
                                position = sTable.position
                            ))
                            projectTables.add(sTable.copy(
                                position = fTable.position
                            ))
                            state.copy(
                                tables = projectTables.sortedBy { it.position },
                                dropdownDialogIndex = -1,
                            )
                        }
                    }
                }
            }
        }
    }


    /**
     * moves 2 table tasks, this is different from swapping positions
     *
     * @param fIndex index of the first task in the table
     * @param sIndex index of the second task in the table
     * @param tableId id of the table in which the tasks are located at, this is used mainly to save
     * the need to find the id manually
     * @throws IllegalArgumentException if { fIndex == sIndex }
     */
    fun moveTableTasks(tableId: Long, fIndex: Int, sIndex: Int) {
        if(fIndex == sIndex) {
            throw IllegalArgumentException("fIndex and sIndex can't be the same")
        }
        val state = (mutableState.value as ProjectTableScreenState.Success)
        coroutineScope.launchIO {
            state.tables.find { table -> table.id == tableId }!!.let { table ->
                table.tasks[fIndex].let { fTask ->
                    table.tasks[sIndex].let { sTask ->
                        if(moveTableTask.await(fTask.id, sTask.id)) {
                            val projectTables = state.tables.toMutableList()
                            projectTables.remove(table)
                            var tasks = table.tasks.toMutableList()
                            tasks = if (sIndex > fIndex) {
                                tasks.mapIndexed { index, it ->
                                    when (index) {
                                        in (fIndex + 1)..sIndex -> it.copy(
                                            position = it.position - 1
                                        )
                                        fIndex -> it.copy(
                                            position = sTask.position
                                        )
                                        else -> it
                                    }
                                }.toMutableList()
                            } else {
                                tasks.mapIndexed { index, it ->
                                    when (index) {
                                        in sIndex until fIndex -> it.copy(
                                            position = it.position + 1
                                        )
                                        fIndex -> it.copy(
                                            position = sTask.position
                                        )
                                        else -> it
                                    }
                                }.toMutableList()
                            }
                            projectTables.add(
                                table.copy(
                                    tasks = tasks.sortedBy { it.position },
                                )
                            )
                            mutableState.update {
                                state.copy(
                                    tables = projectTables.sortedBy { it.position },
                                    taskMoved = state.taskMoved + 1
                                )
                            }
                        } else {
                            mutableState.update {
                                state.copy(
                                    taskMoved = state.taskMoved + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun showDialog(dialog: ProjectTableDialog) {
        val state = (state.value as ProjectTableScreenState.Success)
        when (dialog) {
            is ProjectTableDialog.CreateTable -> {
                coroutineScope.launchUI {
                    mutableState.update {
                        state.copy(
                            dialog = dialog
                        )
                    }
                }
            }
        }
    }

    fun dismissDialog() {
        mutableState.update {
            when (it) {
                is ProjectTableScreenState.Success -> it.copy(dialog = null)
                else -> it
            }
        }
    }

    /**
     * switches the dropdown menu to the selected index, if the given index matches the one that is
     * already stored then the index will be set to -1 instead
     */
    fun switchDropdownMenu(index: Int) {
        val state = (mutableState.value as ProjectTableScreenState.Success)
        coroutineScope.launchUI {
            mutableState.update {
                state.copy(
                    dropdownDialogIndex = if(state.dropdownDialogIndex == index) -1 else index
                )
            }
        }
    }
}

sealed class ProjectTableDialog {
    data class CreateTable(val title: String = "") : ProjectTableDialog()
}

sealed class ProjectTableScreenState {
    
    @Immutable
    object Loading : ProjectTableScreenState()

    /**
     * TODO find a way to get rid of taskMoved, using events does not work properly
     */
    @Immutable
    data class Success(
        val project: Project,
        val tables: List<ProjectTable>,
        val topBarSelectedIndex: Int = 0,
        val dropdownDialogIndex: Int = -1,
        val taskMoved: Int = -1,
        val dialog: ProjectTableDialog? = null,
    ): ProjectTableScreenState()

}