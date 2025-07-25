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

	private val taskRepository: TableTaskApiService = Injekt.get(),
	private val projectTableApiService: ProjectTableApiService = Injekt.get(),
) : StateScreenModel<TableTaskScreenState>(TableTaskScreenState.Loading(taskId)) {
	private val _events: MutableSharedFlow<TableTaskEvent> = MutableSharedFlow()
	val events: SharedFlow<TableTaskEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		requestTaskData(taskId)
	}

	private fun requestTaskData(taskId: Long) {
		mutex.launchIONoQueue(coroutineScope) {
			TableTaskRepository
				.fetchByIdIfStale(taskId)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToRetrieveTask)
					return@launchIONoQueue
				}
			val task = state.value.taskNonComposable()

			ProjectTableRepository
				.fetchByIdIfStale(
					id = task.tableId,
					fetchTasks = true
				)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToRetrieveTask)
					return@launchIONoQueue
				}

			mutableState.update {
				TableTaskScreenState.Success(taskId)
			}
		}
	}

	private fun renameTask(newTitle: String) {

	}

	fun updateDescription(newDescription: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)
			val renamedTask = successState
				.taskNonComposable()
				.copy(description = newDescription)            // val renamedTask = successState.task.copy(description = newDescription)

			val persistedTask = taskRepository
				.updateTask(renamedTask)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToUpdateTaskDescription)
					return@launchIONoQueue
				}
				.getOrThrow()

			val combinedData = TableTaskRepository.combineForUpdate(persistedTask)
			TableTaskRepository.update(combinedData)

			dismissSheet()
		}
	}

	fun swapTable(tableId: Long) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)
			val oldTask = successState.taskNonComposable()

			val persistedTasks = taskRepository
				.moveToTable(
					oldTask.id,
					tableId
				)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToSwapTable)
					return@launchIONoQueue
				}
				.getOrThrow().data

			val combinedData = TableTaskRepository.combineForUpdate(*persistedTasks.toTypedArray())
			TableTaskRepository.update(combinedData)

			dismissSheet()
		}
	}

	fun showSheet(sheet: TableTaskSheet) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			when (sheet) {
				is TableTaskSheet.BottomSheet -> {

					val task = successState.taskNonComposable()
					val table = ProjectTableRepository.dataKeyById(task.tableId)!!

					// the tables are not guaranteed to be fetched up to this point so fetching them
					// if needed
					ProjectTableRepository
						.fetchByProjectIdIfStale(table.projectId)
						.onFailureWithStackTrace {
							_events.emit(TableTaskEvent.FailedToShowSheet)
							return@launchIONoQueue
						}

					mutableState.update {
						successState.copy(
							sheet = sheet,
						)
					}
				}

				else -> {
					mutableState.update { successState.copy(sheet = sheet) }
				}
			}
		}
	}

	fun dismissSheet() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			mutableState.update { successState.copy(sheet = null) }
		}
	}
}

sealed class TableTaskSheet {
	data object BottomSheet : TableTaskSheet()
	data class AlterDescriptionSheet(val description: String = "") : TableTaskSheet()
}

sealed class TableTaskEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : TableTaskEvent()

	data object FailedToSwapTable : LocalizedMessage(R.string.error_failed_table_swap)
	data object FailedToUpdateTaskDescription :
		LocalizedMessage(R.string.error_failed_to_update_task_description)

	data object FailedToRetrieveTask : LocalizedMessage(R.string.error_failed_to_retrieve_task)
	data object FailedToRetrieveTable : LocalizedMessage(R.string.error_failed_to_retrieve_table)
	data object FailedToShowSheet : LocalizedMessage(R.string.error_failed_to_show_sheet)
}

sealed class TableTaskScreenState(open val taskId: Long) {

	fun taskNonComposable(): TableTask = TableTaskRepository.dataKeyById(taskId)!!

	@Immutable data class Loading(override val taskId: Long) : TableTaskScreenState(taskId)

	@Immutable data class Success(
		override val taskId: Long,
		val task: TableTask = TableTask(),
		val parentTable: ProjectTable = ProjectTable(),
		val sheet: TableTaskSheet? = null,
		val sheetTables: List<ProjectTable> = emptyList(),
	) : TableTaskScreenState(taskId) {

		@Composable
		fun task(): TableTask = TableTaskRepository.dataKeyCollectedById(taskId)!!

		@Composable
		fun parentTable(): ProjectTable =
			ProjectTableRepository.dataKeyCollectedById(task().tableId)!!

	}
}