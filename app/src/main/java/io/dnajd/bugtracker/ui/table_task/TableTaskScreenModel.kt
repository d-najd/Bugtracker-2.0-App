package io.dnajd.bugtracker.ui.table_task

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.ProjectTableSharedState
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
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
			val taskResult = async {
				TableTaskRepository
					.fetchByIdIfStale(taskId)
					.onFailureWithStackTrace {
						_events.emit(TableTaskEvent.FailedToRetrieveTask)
						cancel()
					}
			}

			val tableResult =
				async {                // ProjectTableRepository.fetchByProjectIdIfStale()
				}

			/*
			val task = taskRepository
				.getById(taskId)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToRetrieveTask)
					return@launchIONoQueue
				}
				.getOrThrow()

			val table = projectTableApiService
				.getById(
					id = task.tableId,
					includeTasks = true
				)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToRetrieveTable)
					return@launchIONoQueue
				}
				.getOrThrow()

			mutableState.update {
				TableTaskScreenState.Success(
					task = task,
					parentTable = table,
				)
			}
			 */
		}
	}

	private fun renameTask(newTitle: String) {

	}

	fun updateDescription(newDescription: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)
			val renamedTask = successState.task.copy(description = newDescription)

			val persistedTask = taskRepository
				.updateTask(renamedTask)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToUpdateTaskDescription)
					return@launchIONoQueue
				}
				.getOrThrow()

			mutableState.update {
				successState.copy(task = persistedTask)
			}
			dismissSheet()
		}
	}

	fun swapTable(tableId: Long) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			taskRepository
				.moveToTable(
					successState.task.id,
					tableId
				)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToSwapTable)
					return@launchIONoQueue
				}

			val table = projectTableApiService
				.getById(
					id = tableId,
					includeTasks = true
				)
				.onFailureWithStackTrace {
					_events.emit(TableTaskEvent.FailedToSwapTable)
					return@launchIONoQueue
				}
				.getOrThrow()

			mutableState.update {
				successState.copy(
					task = successState.task.copy(
						tableId = tableId,
						position = 0,
					),
					parentTable = table,
				)
			}

			dismissSheet()

			ProjectTableSharedState.notifyTableOrTaskAltered()
		}
	}

	fun showSheet(sheet: TableTaskSheet) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			when (sheet) {
				is TableTaskSheet.BottomSheet -> {
					val tables = successState.sheetTables.ifEmpty {
						projectTableApiService
							.getAllByProjectId(
								projectId = successState.parentTable.projectId,
								includeTasks = true
							)
							.onFailureWithStackTrace {
								_events.emit(TableTaskEvent.FailedToShowSheet)
								return@launchIONoQueue
							}
							.getOrThrow().data
					}

					mutableState.update {
						successState.copy(
							sheetTables = tables,
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

	@Immutable data class Loading(override val taskId: Long) : TableTaskScreenState(taskId)

	@Immutable data class Success(
		override val taskId: Long,
		val task: TableTask,
		val parentTable: ProjectTable,
		val sheet: TableTaskSheet? = null,
		val sheetTables: List<ProjectTable> = emptyList(),
	) : TableTaskScreenState(taskId) {

		@Composable
		fun task(): TableTask =
			TableTaskRepository.        // val projectState by ProjectRepository.state.collectAsState()
				// val projectState = ProjectRepository.projectsAsState()
			dataKeyById(taskId)!!
	}
}