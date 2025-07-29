package io.dnajd.bugtracker.ui.table_task

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.project_table.repository.ProjectTableRepository
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TableTaskStateScreenModel(
	taskId: Long,

	private val taskApiService: TableTaskApiService = Injekt.get(),
	private val projectTableApiService: ProjectTableApiService = Injekt.get(),
) : StateScreenModel<TableTaskScreenState>(TableTaskScreenState.Loading(taskId)) {
	private val _events: MutableSharedFlow<TableTaskEvent> = MutableSharedFlow()
	val events: SharedFlow<TableTaskEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	fun successState(): TableTaskScreenState.Success = (mutableState.value as TableTaskScreenState.Success)

	init {
		requestTaskData(taskId)
	}

	private fun requestTaskData(taskId: Long) = mutex.launchIONoQueue(coroutineScope) {
		val task = TableTaskRepository
			.fetchByIdIfStale(taskId)
			.onFailureWithStackTrace {
				_events.emit(TableTaskEvent.FailedToRetrieveTask)
				return@launchIONoQueue
			}
			.getOrThrow()

		ProjectTableRepository
			.fetchByIdIfStale(
				id = task.tableId,
				fetchTasks = true,
			)
			.onFailureWithStackTrace {
				_events.emit(TableTaskEvent.FailedToRetrieveTask)
				return@launchIONoQueue
			}

		mutableState.update {
			TableTaskScreenState.Success(taskId)
		}
	}

	fun renameTask(newTitle: String) = mutex.launchIONoQueue(coroutineScope) {
		val renamedTask = successState()
			.taskCurrent()
			.copy(title = newTitle)

		val persistedTask = taskApiService
			.updateTask(renamedTask)
			.onFailureWithStackTrace {
				_events.emit(TableTaskEvent.FailedToRenameTask)
				return@launchIONoQueue
			}
			.getOrThrow()

		TableTaskRepository.update(TableTaskRepository.combineForUpdate(persistedTask))
	}

	fun updateDescription(newDescription: String) = mutex.launchIONoQueue(coroutineScope) {
		val renamedTask = successState()
			.taskCurrent()
			.copy(description = newDescription)

		val persistedTask = taskApiService
			.updateTask(renamedTask)
			.onFailureWithStackTrace {
				_events.emit(TableTaskEvent.FailedToUpdateTaskDescription)
				return@launchIONoQueue
			}
			.getOrThrow()

		TableTaskRepository.update(TableTaskRepository.combineForUpdate(persistedTask))

		dismissSheet()
	}

	fun swapTable(tableId: Long) = mutex.launchIONoQueue(coroutineScope) {
		val oldTask = successState().taskCurrent()

		val persistedTasks = taskApiService
			.moveToTable(
				oldTask.id,
				tableId,
			)
			.onFailureWithStackTrace {
				_events.emit(TableTaskEvent.FailedToSwapTable)
				return@launchIONoQueue
			}
			.getOrThrow().data

		TableTaskRepository.update(TableTaskRepository.combineForUpdate(*persistedTasks.toTypedArray()))

		dismissSheet()
	}

	fun showSheet(sheet: TableTaskSheet) = mutex.launchIONoQueue(coroutineScope) {
		when (sheet) {
			is TableTaskSheet.BottomSheet -> {
				val task = successState().taskCurrent()
				val table = ProjectTableRepository
					.dataKeysById(task.tableId)
					.first()

				// the tables are not guaranteed to be fetched up to this point so fetching them
				// if needed
				ProjectTableRepository
					.fetchByProjectIdIfStale(table.projectId)
					.onFailureWithStackTrace {
						_events.emit(TableTaskEvent.FailedToShowSheet)
						return@launchIONoQueue
					}

				mutableState.update {
					successState().copy(
						sheet = sheet,
					)
				}
			}

			else -> {
				mutableState.update { successState().copy(sheet = sheet) }
			}
		}
	}

	fun dismissSheet() = mutex.launchUINoQueue(coroutineScope) {
		mutableState.update { successState().copy(sheet = null) }
	}
}

sealed class TableTaskSheet {
	data object BottomSheet : TableTaskSheet()
	data class AlterDescriptionSheet(val description: String = "") : TableTaskSheet()
}

sealed class TableTaskEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : TableTaskEvent()

	data object FailedToRenameTask : LocalizedMessage(R.string.error_failed_to_rename_task)

	data object FailedToSwapTable : LocalizedMessage(R.string.error_failed_table_swap)
	data object FailedToUpdateTaskDescription :
		LocalizedMessage(R.string.error_failed_to_update_task_description)

	data object FailedToRetrieveTask : LocalizedMessage(R.string.error_failed_to_retrieve_task)
	data object FailedToRetrieveTable : LocalizedMessage(R.string.error_failed_to_retrieve_table)
	data object FailedToShowSheet : LocalizedMessage(R.string.error_failed_to_show_sheet)
}

sealed class TableTaskScreenState(open val taskId: Long) {
	fun taskCurrent(): TableTask = TableTaskRepository.dataKeysById(taskId).first()

	@Immutable data class Loading(override val taskId: Long) : TableTaskScreenState(taskId)

	@Immutable data class Success(
		override val taskId: Long,
		val sheet: TableTaskSheet? = null,
	) : TableTaskScreenState(taskId) {

		@Composable
		fun taskCollected(): TableTask = TableTaskRepository.dataKeysCollectedById(taskId).first()

		@Composable
		fun parentTableCollected(): ProjectTable = ProjectTableRepository
			.dataKeysCollectedByProjectId(taskCollected().tableId)
			.first()

		@Composable
		fun sheetTablesCollected(): List<ProjectTable> = ProjectTableRepository
			.dataKeysByProjectIds(parentTableCollected().projectId)
			.toList()
	}
}
