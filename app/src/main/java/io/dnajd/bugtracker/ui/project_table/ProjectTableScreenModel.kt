package io.dnajd.bugtracker.ui.project_table

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

@OptIn(ExperimentalCoroutinesApi::class) class ProjectTableScreenModel(
	private val projectId: Long,

	private val projectApiService: ProjectApiService = Injekt.get(),
	private val projectTableApiService: ProjectTableApiService = Injekt.get(),
	private val tableTaskApiService: TableTaskApiService = Injekt.get(),
) : StateScreenModel<ProjectTableScreenState>(ProjectTableScreenState.Loading) {
	private val _events: Channel<ProjectTableEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<ProjectTableEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			fetchTableData(this)
		}
	}

	fun reFetchTableData() {
		mutex.launchIONoQueue(coroutineScope) {
			mutableState.update {
				ProjectTableScreenState.Loading
			}

			fetchTableData(this)
		}
	}

	private suspend fun fetchTableData(coroutineScope: CoroutineScope) {
		val projectResult = coroutineScope.async {
			projectApiService
				.getById(projectId)
				.onFailure { cancel() }
		}
		val tablesResult = coroutineScope.async {
			this@ProjectTableScreenModel.projectTableApiService
				.getAllByProjectId(this@ProjectTableScreenModel.projectId)
				.onFailure { this.cancel() }
		}

		awaitAll(
			projectResult,
			tablesResult
		)

		if (projectResult.isCancelled || tablesResult.isCancelled) {
			projectResult
				.getCompletionExceptionOrNull()
				?.printStackTrace()
			tablesResult
				.getCompletionExceptionOrNull()
				?.printStackTrace()

			return
		}

		val project = projectResult
			.getCompleted()
			.getOrThrow()
		val tables = tablesResult
			.getCompleted()
			.getOrThrow()
		val sortedTables = tables.data
			.sortedBy { it.position }
			.map { table -> table.copy(tasks = table.tasks.sortedBy { it.position }) }

		mutableState.update {
			ProjectTableScreenState.Success(
				project = project,
				tables = sortedTables,
			)
		}
	}

	fun createTable(table: ProjectTable) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			val createdTable = projectTableApiService
				.createTable(table)
				.onFailureWithStackTrace {
					_events.send(ProjectTableEvent.FailedToCreateProjectTable)
					return@launchIONoQueue
				}
				.getOrThrow()

			val tables = successState.tables.toMutableList()
			tables.add(createdTable)

			mutableState.update { successState.copy(tables = tables) }

			_events.send(ProjectTableEvent.CreatedTable)
		}
	}

	fun showCreateTaskMenu(tableId: Long?) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			mutableState.update { successState.copy(createTableItemSelectedTableId = tableId) }
		}
	}

	fun createTask(task: TableTask) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			val createdTask = tableTaskApiService
				.createTask(task)
				.onFailureWithStackTrace {
					_events.send(ProjectTableEvent.FailedToCreateTableTask)
					return@launchIONoQueue
				}
				.getOrThrow()

			val tables = successState.tables.toMutableList()
			val table = tables.find { it.id == task.tableId }!!
			tables.remove(table)

			val tasks = table.tasks.toMutableList()
			tasks.add(createdTask)

			tables.add(table.copy(tasks = tasks))

			mutableState.update {
				successState.copy(
					tables = tables,
					createTableItemSelectedTableId = null,
				)
			}
		}
	}

	fun renameTable(
		id: Long,
		newName: String,
	) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success
			val tables = successState.tables.toMutableList()
			val table = tables.find { table -> table.id == id }!!
			val renamedTable = table.copy(title = newName)

			val persistedTable = projectTableApiService
				.updateTable(renamedTable)
				.onFailureWithStackTrace {
					_events.send(ProjectTableEvent.FailedToRenameProjectTable)
					return@launchIONoQueue
				}
				.getOrThrow()

			tables.remove(table)
			tables.add(persistedTable)

			mutableState.update {
				successState.copy(tables = tables)
			}

			_events.send(ProjectTableEvent.RenamedTable)
		}
	}

	/**
	 * swaps the positions of 2 tables
	 * @param fId id of the first table
	 * @param sId id of the second table
	 * @throws IllegalArgumentException if [fId] == [sId]
	 */
	fun swapTablePositions(
		fId: Long,
		sId: Long,
	) {
		if (fId == sId) throw IllegalArgumentException("Can't swap table with itself")

		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			val tables = successState.tables.toMutableList()

			val fTable = tables.find { table -> table.id == sId }!!
			val sTable = tables.find { table -> table.id == fId }!!

			projectTableApiService
				.swapTablePositions(
					fId,
					sId
				)
				.onFailureWithStackTrace {
					_events.send(ProjectTableEvent.FailedToSwapTablePositions)
					return@launchIONoQueue
				}

			tables.remove(fTable)
			tables.remove(sTable)

			tables.add(
				fTable.copy(
					position = sTable.position
				)
			)
			tables.add(
				sTable.copy(
					position = fTable.position
				)
			)

			mutableState.update {
				successState.copy(
					tables = tables.sortedBy { it.position },
					dropdownDialogSelectedTableId = null,
				)
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
	 * @throws IllegalArgumentException if [fIndex] == [sIndex]
	 */
	fun moveTableTasks(
		tableId: Long,
		fIndex: Int,
		sIndex: Int,
	) {
		if (fIndex == sIndex) {
			throw IllegalArgumentException("fIndex and sIndex can't be the same")
		}

		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			val tables = successState.tables.toMutableList()

			val table = tables.find { table -> table.id == tableId }!!
			val fTask = table.tasks[fIndex]
			val sTask = table.tasks[sIndex]

			tableTaskApiService
				.movePositionTo(
					fTask.id,
					sTask.id
				)
				.onFailureWithStackTrace {                    // TODO what the hell is this?
					mutableState.update {
						successState.copy(
							manualTableTasksRefresh = successState.manualTableTasksRefresh + 1
						)
					}
					_events.send(ProjectTableEvent.FailedToMoveTableTasks)
					return@launchIONoQueue
				}

			ifMoveTaskSuccess(
				tables = tables,
				table = table,
				sTask = sTask,
				fIndex = fIndex,
				sIndex = sIndex,
			)
		}
	}

	private fun ifMoveTaskSuccess(
		tables: MutableList<ProjectTable>,
		table: ProjectTable,
		sTask: TableTask,
		fIndex: Int,
		sIndex: Int,
	) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		tables.remove(table)
		var tasks = table.tasks.toMutableList()
		tasks = if (sIndex > fIndex) {
			tasks
				.mapIndexed { index, it ->
					when (index) {
						in (fIndex + 1)..sIndex -> it.copy(
							position = it.position - 1
						)

						fIndex -> it.copy(
							position = sTask.position
						)

						else -> it
					}
				}
				.toMutableList()
		} else {
			tasks
				.mapIndexed { index, it ->
					when (index) {
						in sIndex until fIndex -> it.copy(
							position = it.position + 1
						)

						fIndex -> it.copy(
							position = sTask.position
						)

						else -> it
					}
				}
				.toMutableList()
		}
		tables.add(
			table.copy(
				tasks = tasks.sortedBy { it.position },
			)
		)

		mutableState.update {
			successState.copy(
				tables = tables.sortedBy { it.position },
				manualTableTasksRefresh = successState.manualTableTasksRefresh + 1
			)
		}
	}

	fun deleteTable(tableId: Long) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			projectTableApiService
				.deleteById(tableId)
				.onSuccess {
					val tables = successState.tables.toMutableList()
					tables.removeIf { it.id == tableId }

					mutableState.update {
						successState.copy(
							tables = tables
						)
					}
				}
				.onFailure {
					it.printStackTrace()
					_events.send(ProjectTableEvent.FailedToDeleteTable)
				}
		}
	}

	fun showDialog(dialog: ProjectTableDialog) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			mutableState.update { successState.copy(dialog = dialog) }
		}
	}

	fun dismissDialog() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			mutableState.update { successState.copy(dialog = null) }
		}
	}

	fun switchDropdownMenu(tableId: Long?) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as ProjectTableScreenState.Success

			val newTableId =
				if (successState.dropdownDialogSelectedTableId == tableId) null else tableId

			mutableState.update { successState.copy(dropdownDialogSelectedTableId = newTableId) }
		}
	}
}

sealed class ProjectTableEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectTableEvent()

	data object CreatedTable : ProjectTableEvent()

	data object FailedToCreateProjectTable :
		LocalizedMessage(R.string.error_failed_to_create_project_table)

	data object FailedToCreateTableTask :
		LocalizedMessage(R.string.error_failed_to_create_table_task)

	data object RenamedTable : ProjectTableEvent()

	data object FailedToRenameProjectTable :
		LocalizedMessage(R.string.error_failed_to_rename_project_table)

	data object FailedToSwapTablePositions :
		LocalizedMessage(R.string.error_failed_to_swap_table_positions)

	data object FailedToMoveTableTasks : LocalizedMessage(R.string.error_failed_to_move_table_tasks)

	data object FailedToDeleteTable : LocalizedMessage(R.string.error_failed_to_delete_table)
}

sealed class ProjectTableDialog {
	data class CreateTable(val title: String = "") : ProjectTableDialog()
	data class RenameTable(
		val id: Long,
		val title: String = "",
	) : ProjectTableDialog()
}

sealed class ProjectTableScreenState {

	@Immutable data object Loading : ProjectTableScreenState()

	/**
	 * TODO find a way to get rid of taskMoved, using events does not work properly
	 */
	@Immutable data class Success(
		val project: Project,
		val tables: List<ProjectTable>,
		val dropdownDialogSelectedTableId: Long? = null,
		/** This is used in the bottom portion of the table specifically the create button */
		val createTableItemSelectedTableId: Long? = null,        // TODO remove this
		val manualTableTasksRefresh: Int = 0,
		val dialog: ProjectTableDialog? = null,
	) : ProjectTableScreenState()

}