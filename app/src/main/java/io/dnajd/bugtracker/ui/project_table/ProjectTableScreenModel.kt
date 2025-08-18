package io.dnajd.bugtracker.ui.project_table

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.project.repository.ProjectRepository
import io.dnajd.data.project_table.repository.ProjectTableRepository
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.domain.base.onFailureWithStackTrace
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectTableScreenModel(
	private val projectId: Long,

	private val projectTableApiService: ProjectTableApiService = Injekt.get(),
	private val tableTaskApiService: TableTaskApiService = Injekt.get(),
) : StateScreenModel<ProjectTableScreenState>(ProjectTableScreenState.Loading(projectId)) {
	private val _events: MutableSharedFlow<ProjectTableEvent> = MutableSharedFlow()
	val events: SharedFlow<ProjectTableEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			fetchTableData(this)
		}
	}

	private suspend fun fetchTableData(coroutineScope: CoroutineScope) {
		val projectResult = coroutineScope.async {
			ProjectRepository
				.fetchOneIfStale(projectId)
				.onFailure {
					cancel()
				}
		}
		val tablesResult = coroutineScope.async {
			ProjectTableRepository
				.fetchByProjectIdIfStale(
					projectId,
					fetchTasks = true,
				)
				.onFailure { this.cancel() }
		}

		awaitAll(
			projectResult,
			tablesResult,
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

		mutableState.update {
			ProjectTableScreenState.Success(
				events = events,
				projectId = projectId,
			)
		}
	}

	fun createTable(table: ProjectTable) = mutex.launchIONoQueue(coroutineScope) {
		val createdTable = projectTableApiService
			.createTable(table)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToCreateProjectTable)
				return@launchIONoQueue
			}
			.getOrThrow()

		ProjectTableRepository.update(ProjectTableRepository.combineForUpdate(createdTable))

		_events.emit(ProjectTableEvent.CreatedTable)
	}

	fun showCreateTaskMenu(tableId: Long?) = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		mutableState.update { successState.copy(taskBeingAddedInTableId = tableId) }
	}

	fun createTask(task: TableTask) = mutex.launchIONoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		val createdTask = tableTaskApiService
			.createTask(task)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToCreateTableTask)
				return@launchIONoQueue
			}
			.getOrThrow()

		TableTaskRepository.update(TableTaskRepository.combineForUpdate(createdTask))

		mutableState.update {
			successState.copy(
				taskBeingAddedInTableId = null,
			)
		}
	}

	fun renameTable(
		id: Long,
		newName: String,
	) = mutex.launchIONoQueue(coroutineScope) {
		val table = ProjectTableRepository
			.dataKeysById(id)
			.first()
		val renamedTable = table.copy(title = newName)

		val persistedTable = projectTableApiService
			.updateTable(renamedTable)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToRenameProjectTable)
				return@launchIONoQueue
			}
			.getOrThrow()

		ProjectTableRepository.update(ProjectTableRepository.combineForUpdate(persistedTable))

		_events.emit(ProjectTableEvent.RenamedTable)
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
	) = mutex.launchIONoQueue(coroutineScope) {
		if (fId == sId) throw IllegalArgumentException("Can't swap table with itself")

		val successState = mutableState.value as ProjectTableScreenState.Success

		val persistedTasks = projectTableApiService
			.swapTablePositions(
				fId,
				sId,
			)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToSwapTablePositions)
				return@launchIONoQueue
			}
			.getOrThrow().data

		val combinedData = ProjectTableRepository.combineForUpdate(*persistedTasks.toTypedArray())
		ProjectTableRepository.update(combinedData)

		mutableState.update {
			successState.copy(
				dropdownOpenedInTableId = null,
			)
		}
	}

	/**
	 * moves 2 table tasks, this is different from swapping positions, the tasks must be in the same table
	 *
	 * @param fId id of the first task
	 * @param sId id of the second task
	 * @throws IllegalArgumentException if [fId] == [sId] or if the [TableTask.tableId] aren't the same
	 */
	fun moveTableTasks(
		fId: Long,
		sId: Long,
	) = mutex.launchIONoQueue(coroutineScope) {
		if (fId == sId) {
			throw IllegalArgumentException("fIndex and sIndex can't be the same")
		}

		val fTask = TableTaskRepository.dataKeysById(fId)
			.first()
		val sTask = TableTaskRepository.dataKeysById(sId)
			.first()

		if (fTask.tableId != sTask.tableId) {
			throw IllegalArgumentException("Trying to move tasks between different tables")
		}

		val persistedTasks = tableTaskApiService
			.movePositionTo(
				fTask.id,
				sTask.id,
			)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToMoveTableTasks)
				return@launchIONoQueue
			}
			.getOrThrow().data

		val combinedData = TableTaskRepository.combineForUpdate(*persistedTasks.toTypedArray())
		TableTaskRepository.update(combinedData)
	}

	fun deleteTable(tableId: Long) = mutex.launchIONoQueue(coroutineScope) {
		val otherModifiedTables = projectTableApiService
			.deleteById(tableId)
			.onFailureWithStackTrace {
				_events.emit(ProjectTableEvent.FailedToDeleteTable)
				return@launchIONoQueue
			}
			.getOrThrow().data

		ProjectTableRepository.delete(tableId)
		val combinedData =
			ProjectTableRepository.combineForUpdate(newData = otherModifiedTables.toTypedArray())
		ProjectTableRepository.update(combinedData)
	}

	fun showDialog(dialog: ProjectTableDialog) = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		mutableState.update { successState.copy(dialog = dialog) }
	}

	fun dismissDialog() = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		mutableState.update { successState.copy(dialog = null) }
	}

	fun switchDropdownMenu(tableId: Long?) = mutex.launchUINoQueue(coroutineScope) {
		val successState = mutableState.value as ProjectTableScreenState.Success

		val newTableId = if (successState.dropdownOpenedInTableId == tableId) null else tableId

		mutableState.update { successState.copy(dropdownOpenedInTableId = newTableId) }
	}
}

sealed class ProjectTableEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectTableEvent()

	data object CreatedTable : ProjectTableEvent()

	data object FailedToCreateProjectTable : LocalizedMessage(R.string.error_failed_to_create_project_table)

	data object FailedToCreateTableTask : LocalizedMessage(R.string.error_failed_to_create_table_task)

	data object RenamedTable : ProjectTableEvent()

	data object FailedToRenameProjectTable : LocalizedMessage(R.string.error_failed_to_rename_project_table)

	data object FailedToSwapTablePositions : LocalizedMessage(R.string.error_failed_to_swap_table_positions)

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

sealed class ProjectTableScreenState(open val projectId: Long) {

	@Immutable
	data class Loading(override val projectId: Long) : ProjectTableScreenState(projectId)

	@Immutable
	data class Success(
		override val projectId: Long,
		val events: Flow<ProjectTableEvent>,
		val dropdownOpenedInTableId: Long? = null,
		/** This is used in the bottom portion of the table specifically the create button */
		val taskBeingAddedInTableId: Long? = null,
		val dialog: ProjectTableDialog? = null,
	) : ProjectTableScreenState(projectId) {
		@Composable
		fun projectCollected(): Project = ProjectRepository
			.dataKeysCollectedById(projectId)
			.first()

		@Composable
		fun tablesCollected(): Set<ProjectTable> =
			ProjectTableRepository.dataKeysCollectedByProjectId(projectId)

		@Composable
		fun tasksCollectedByTableId(tableId: Long): Set<TableTask> =
			TableTaskRepository.dataKeysCollectedByTableId(tableId)
	}
}
