package io.dnajd.bugtracker.ui.project_table

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.interactor.*
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
    private val deleteTable: DeleteProjectTable = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectTableScreenState>(context, ProjectTableScreenState.Loading) {

    init {
        requestTables(project)
    }

    private fun requestTables(project: Project) {
        coroutineScope.launchIO {
            val tables = getTables.await(project.id).sortedBy { it.position }.map { table ->
                table.copy(
                    tasks = table.tasks.sortedBy { it.position }
                )
            }
            mutableState.update {
                ProjectTableScreenState.Success(
                    project = project,
                    tables = tables,
                )
            }
        }
    }

    fun createTable(table: ProjectTable) {
        coroutineScope.launchIO {
            createTable.awaitOne(table)?.let { persistedTable ->
                val tables = (mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
                tables.add(persistedTable)
                mutableState.update {
                    (mutableState.value as ProjectTableScreenState.Success).copy(
                        tables = tables,
                    )
                }
            }
        }
    }

    fun showCreateTaskMenu(tableId: Long) {
        coroutineScope.launchUI {
            mutableState.update {
                (mutableState.value as ProjectTableScreenState.Success).copy(
                    createTableItemSelectedTableId = tableId,
                )
            }
        }
    }

    fun renameTable(id: Long, newName: String) {
        coroutineScope.launchIO {
            (mutableState.value as ProjectTableScreenState.Success).tables.find { table -> table.id == id }!!.let { originalTable ->
                if(renameTable.await(id, newName)) {
                    // doing it this way so that state changes get updated for sure
                    mutableState.update {
                        val tables = (mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
                        tables.remove(originalTable)
                        tables.add(originalTable.copy(title = newName))
                        (mutableState.value as ProjectTableScreenState.Success).copy(
                            tables = tables
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
            (mutableState.value as ProjectTableScreenState.Success).tables.find { table -> table.id == sId }!!.let { fTable ->
                (mutableState.value as ProjectTableScreenState.Success).tables.find { table -> table.id == fId }!!.let { sTable ->
                    if(swapTables.await(fId, sId)) {
                        // doing it this way so that state changes get updated for sure
                        mutableState.update {
                            val tables = (mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
                            tables.remove(fTable)
                            tables.remove(sTable)
                            tables.add(fTable.copy(
                                position = sTable.position
                            ))
                            tables.add(sTable.copy(
                                position = fTable.position
                            ))
                            (mutableState.value as ProjectTableScreenState.Success).copy(
                                tables = tables.sortedBy { it.position },
                                dropdownDialogSelectedTableId = null,
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
        coroutineScope.launchIO {
            (mutableState.value as ProjectTableScreenState.Success).tables.find { table -> table.id == tableId }!!.let { table ->
                table.tasks[fIndex].let { fTask ->
                    table.tasks[sIndex].let { sTask ->
                        if(moveTableTask.await(fTask.id, sTask.id)) {
                            val tables = (mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
                            tables.remove(table)
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
                            tables.add(
                                table.copy(
                                    tasks = tasks.sortedBy { it.position },
                                )
                            )
                            mutableState.update {
                                (mutableState.value as ProjectTableScreenState.Success).copy(
                                    tables = tables.sortedBy { it.position },
                                    taskMoved = (mutableState.value as ProjectTableScreenState.Success).taskMoved + 1
                                )
                            }
                        } else {
                            mutableState.update {
                                (mutableState.value as ProjectTableScreenState.Success).copy(
                                    taskMoved = (mutableState.value as ProjectTableScreenState.Success).taskMoved + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteTable(tableId: Long) {
        coroutineScope.launchIO {
            if(deleteTable.awaitOne(tableId)) {
                mutableState.update {
                    val tables = (mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
                    tables.removeIf { it.id == tableId }
                    (mutableState.value as ProjectTableScreenState.Success).copy(
                        tables = tables
                    )
                }
            }
        }
    }

    fun showDialog(dialog: ProjectTableDialog) {
        @Suppress("UNUSED_EXPRESSION")
        when (dialog) {
            else -> {
                coroutineScope.launchUI {
                    mutableState.update {
                        (mutableState.value as ProjectTableScreenState.Success).copy(
                            dialog = dialog,
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
    fun switchDropdownMenu(index: Int?) {
        coroutineScope.launchUI {
            mutableState.update {
                (mutableState.value as ProjectTableScreenState.Success).copy(
                    dropdownDialogSelectedTableId = if((mutableState.value as ProjectTableScreenState.Success)
                            .dropdownDialogSelectedTableId == index) null else index
                )
            }
        }
    }
}

sealed class ProjectTableDialog {
    data class CreateTable(val title: String = "") : ProjectTableDialog()
    data class RenameTable(val id: Long, val title: String = "") : ProjectTableDialog()
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
        val topBarSelected: ProjectTableSelectedTab = ProjectTableSelectedTab.BOARD,
        val dropdownDialogSelectedTableId: Int? = null,
        /**
         * index of the table that is being created item on,
         *
         * this is used on the bottom portion of the table specifically the create button.
         */
        val createTableItemSelectedTableId: Long? = null,
        val taskMoved: Int = 0,
        val dialog: ProjectTableDialog? = null,
    ): ProjectTableScreenState()

}