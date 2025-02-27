package io.dnajd.bugtracker.ui.project_table

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.model.TableTaskBasic
import io.dnajd.domain.table_task.model.toBasic
import io.dnajd.domain.table_task.service.TableTaskRepository
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectTableScreenModel(
	projectId: Long,

	private val projectRepository: ProjectRepository = Injekt.get(),
	private val projectTableRepository: ProjectTableRepository = Injekt.get(),
	private val tableTaskRepository: TableTaskRepository = Injekt.get(),

	/*
	private val getTables: GetProjectTable = Injekt.get(),
	private val createTable: CreateProjectTable = Injekt.get(),
	private val createTask: CreateTableTask = Injekt.get(),
	private val renameTable: RenameProjectTable = Injekt.get(),
	private val swapTables: SwapProjectTables = Injekt.get(),
	private val moveTask: MoveTableTask = Injekt.get(),
	private val deleteTable: DeleteProjectTable = Injekt.get(),
	 */
) : StateScreenModel<ProjectTableScreenState>(ProjectTableScreenState.Loading) {
	init {
		coroutineScope.launchIO {
			val projectResult = async { projectRepository.get(projectId).onFailure { cancel() } }
			val tablesResult =
				async { projectTableRepository.getAll(projectId).onFailure { cancel() } }

			awaitAll(projectResult, tablesResult)

			if (projectResult.isCancelled || tablesResult.isCancelled) {
				projectResult.getCompletionExceptionOrNull()?.printStackTrace()
				tablesResult.getCompletionExceptionOrNull()?.printStackTrace()

				return@launchIO
			}

			var project = projectResult.getCompleted().getOrThrow()
			var tablesHolder = tablesResult.getCompleted().getOrThrow()

			val sortedTables = tablesHolder.data
				.sortedBy { it.position }
				.map { table -> table.copy(tasks = table.tasks.sortedBy { it.position }) }


			/*
			
			val persistedProject = projectRepository.get(projectId)
			persistedProject.onFailure { e ->
				// context.toast("Failed to retrieve project with id $projectId")
				e.printStackTrace()

				return@launchIO
			}

			val persistedTables = retrieveTables(projectId)
			mutableState.update {
				ProjectTableScreenState.Success(
					project = persistedProject.getOrThrow(),
					tables = persistedTables,
				)
			}
			 */
		}
	}

	private suspend fun retrieveTables(projectId: Long): List<ProjectTable> {
		return getTables.await(projectId)
			.sortedBy { it.position }.map { table ->
				table.copy(
					tasks = table.tasks.sortedBy { it.position }
				)
			}
	}

	fun createTable(table: ProjectTable) {
		coroutineScope.launchIO {
			createTable.awaitOne(table)?.let { persistedTable ->
				val tables =
					(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
				tables.add(persistedTable)
				mutableState.update {
					(mutableState.value as ProjectTableScreenState.Success).copy(
						tables = tables,
					)
				}
			}
		}
	}

	fun showCreateTaskMenu(tableId: Long?) {
		coroutineScope.launchUI {
			mutableState.update {
				(mutableState.value as ProjectTableScreenState.Success).copy(
					createTableItemSelectedTableId = tableId,
				)
			}
		}
	}

	fun createTask(task: TableTask) {
		coroutineScope.launchIO {
			createTask.awaitOne(task)?.let { persistedTask ->
				val tables =
					(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
				val table = tables.find { it.id == task.tableId }!!
				tables.remove(table)
				val tasks = table.tasks.toMutableList()
				tasks.add(persistedTask.toBasic())
				tables.add(
					table.copy(
						tasks = tasks,
					)
				)
				mutableState.update {
					(mutableState.value as ProjectTableScreenState.Success).copy(
						tables = tables,
						createTableItemSelectedTableId = null,
					)
				}
			}
		}
	}

	fun renameTable(id: Long, newName: String) {
		coroutineScope.launchIO {
			val tables =
				(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
			tables.find { table -> table.id == id }!!.let { transientTable ->
				if (renameTable.await(id, newName)) {
					// doing it this way so that state changes get updated for sure
					mutableState.update {
						tables.remove(transientTable)
						tables.add(transientTable.copy(title = newName))
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
			val tables =
				(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
			tables.find { table -> table.id == sId }!!.let { fTable ->
				tables.find { table -> table.id == fId }!!.let { sTable ->
					if (swapTables.await(fId, sId)) {
						// doing it this way so that state changes get updated for sure
						mutableState.update {
							tables.remove(fTable)
							tables.remove(sTable)
							tables.add(
								fTable.copy(
									position = sTable.position
								)
							)
							tables.add(
								sTable.copy
								/** This is used in the bottom portion of the table specifically the create button */
									(
									position = fTable.position
								)
							)
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
		if (fIndex == sIndex) {
			throw IllegalArgumentException("fIndex and sIndex can't be the same")
		}
		coroutineScope.launchIO {
			val tables =
				(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
			tables.find { table -> table.id == tableId }!!.let { table ->
				table.tasks[fIndex].let { fTask ->
					table.tasks[sIndex].let { sTask ->
						if (moveTask.await(fTask.id, sTask.id)) {
							ifMoveTaskSuccess(
								tables = tables,
								table = table,
								sTask = sTask,
								fIndex = fIndex,
								sIndex = sIndex,
							)
						} else {
							mutableState.update {
								(mutableState.value as ProjectTableScreenState.Success).copy(
									manualTableTasksRefresh = (mutableState.value as ProjectTableScreenState.Success).manualTableTasksRefresh + 1
								)
							}
						}
					}
				}
			}
		}
	}

	private fun ifMoveTaskSuccess(
		tables: MutableList<ProjectTable>,
		table: ProjectTable,
		sTask: TableTaskBasic,
		fIndex: Int,
		sIndex: Int,
	) {
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
				manualTableTasksRefresh = (mutableState.value as ProjectTableScreenState.Success).manualTableTasksRefresh + 1
			)
		}
	}

	fun deleteTable(tableId: Long) {
		coroutineScope.launchIO {
			if (deleteTable.awaitOne(tableId)) {
				mutableState.update {
					val tables =
						(mutableState.value as ProjectTableScreenState.Success).tables.toMutableList()
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

	fun switchDropdownMenu(tableId: Long?) {
		coroutineScope.launchUI {
			mutableState.update {
				(mutableState.value as ProjectTableScreenState.Success).copy(
					dropdownDialogSelectedTableId = if ((mutableState.value as ProjectTableScreenState.Success)
							.dropdownDialogSelectedTableId == tableId
					) null else tableId
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
		val dropdownDialogSelectedTableId: Long? = null,
		/** This is used in the bottom portion of the table specifically the create button */
		val createTableItemSelectedTableId: Long? = null,
		val manualTableTasksRefresh: Int = 0,
		val dialog: ProjectTableDialog? = null,
	) : ProjectTableScreenState()

}