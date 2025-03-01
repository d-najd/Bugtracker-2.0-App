package io.dnajd.bugtracker.ui.table_task

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository
import io.dnajd.util.launchIO
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

class TableTaskStateScreenModel(
	taskId: Long,

	private val tableTaskRepository: TableTaskRepository = Injekt.get(),
	private val projectTableRepository: ProjectTableRepository = Injekt.get(),

	/*
	private val getTableTask: GetTableTask = Injekt.get(),
	private val getProjectTable: GetProjectTable = Injekt.get(),
	private val swapTableTaskTable: SwapTableTaskTable = Injekt.get(),
	private val updateTaskDescription: UpdateTableTaskDescription = Injekt.get(),
	 */
) : StateScreenModel<TableTaskScreenState>(TableTaskScreenState.Loading) {
	private val _events: Channel<TableTaskEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<TableTaskEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		requestTaskData(taskId)
	}

	private fun requestTaskData(taskId: Long) {
		coroutineScope.launchIO {
			val taskResult = tableTaskRepository.get(taskId).onFailure {
				it.printStackTrace()
				_events.send(TableTaskEvent.FailedToRetrieveTask)
				return@launchIO
			}
			val task = taskResult.getOrThrow()

			projectTableRepository
				.getOne(
					id = task.tableId,
					ignoreTasks = true
				).onSuccess { table ->
					mutableState.update {
						TableTaskScreenState.Success(
							task = task,
							parentTable = table,
						)
					}
				}.onFailure {
					it.printStackTrace()
					_events.send(TableTaskEvent.FailedToRetrieveTable)
				}
		}
	}

	private fun renameTask(newTitle: String) {

	}

	fun updateDescription(newDescription: String) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			tableTaskRepository.updateNoBody(
				id = successState.task.id,
				description = newDescription
			).onSuccess {
				mutableState.update {
					successState.copy(
						task = successState.task.copy(
							description = newDescription,
							updatedAt = Date(),
						)
					)
				}

				dismissSheet()
			}.onFailure {
				it.printStackTrace()
				_events.send(TableTaskEvent.FailedToUpdateTaskDescription)
			}
		}
	}

	fun swapTable(tableId: Long) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			tableTaskRepository.swapTable(successState.task.id, tableId)
				.onFailure {
					it.printStackTrace()
					_events.send(TableTaskEvent.FailedToSwapTable)
					return@launchIONoQueue
				}

			projectTableRepository.getOne(id = tableId, ignoreTasks = true).onSuccess { table ->
				mutableState.update {
					successState.copy(
						task = successState.task.copy(
							tableId = tableId,
							position = 0,
						),
						parentTable = table,
					)
				}
			}.onFailure {
				it.printStackTrace()
				_events.send(TableTaskEvent.FailedToSwapTable)
			}
		}
	}

	fun showSheet(sheet: TableTaskSheet) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			when (sheet) {
				is TableTaskSheet.BottomSheet -> {
					val tables = sheet.tables.ifEmpty {
						val data = projectTableRepository.getAll(
							projectId = successState.parentTable.projectId,
							ignoreTasks = true
						).onFailure {
							it.printStackTrace()
							_events.send(TableTaskEvent.FailedToShowSheet)
						}

						data.getOrThrow().data
					}

					mutableState.update {
						successState.copy(
							sheet = sheet.copy(
								tables = tables,
							),
						)
					}
				}

				else -> {
					mutableState.update {
						successState.copy(
							sheet = sheet,
						)
					}
				}
			}
		}
	}

	fun dismissSheet() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = (mutableState.value as TableTaskScreenState.Success)

			mutableState.update {
				successState.copy(
					sheet = null,
				)
			}
		}
	}
}

sealed class TableTaskSheet {
	data class BottomSheet(val tables: List<ProjectTable> = emptyList()) : TableTaskSheet()
	data class AlterDescriptionSheet(val description: String = "") : TableTaskSheet()
}

sealed class TableTaskEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : TableTaskEvent()

	object FailedToSwapTable : LocalizedMessage(R.string.error_failed_table_swap)
	object FailedToUpdateTaskDescription :
		LocalizedMessage(R.string.error_failed_to_update_task_description)

	object FailedToRetrieveTask : LocalizedMessage(R.string.error_failed_to_retrieve_task)
	object FailedToRetrieveTable : LocalizedMessage(R.string.error_failed_to_retrieve_table)
	object FailedToShowSheet : LocalizedMessage(R.string.error_failed_to_show_sheet)
}

sealed class TableTaskScreenState {

	@Immutable
	object Loading : TableTaskScreenState()

	@Immutable
	data class Success(
		val task: TableTask,
		val parentTable: ProjectTable,
		val sheet: TableTaskSheet? = null,
	) : TableTaskScreenState()

}