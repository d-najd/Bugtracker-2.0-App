package io.dnajd.data.table_task.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.task_assigned.repository.TaskAssignedRepository
import io.dnajd.data.task_comment.repository.TaskCommentRepository
import io.dnajd.data.task_label.repository.TaskLabelRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class TableTaskRepositoryState(
	override val data: Map<TableTask, Date> = emptyMap(),
	val lastFetchesByTableIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TableTask, Date>(data)

object TableTaskRepository :
	RepositoryBase<TableTask, Long, Date, TableTaskRepositoryState>(TableTaskRepositoryState()) {

	private val api: TableTaskApiService = Injekt.get()

	/**
	 * @return true if task has sub data like [TableTask.comments], [TableTask.assigned]... fetched
	 * false otherwise (false if the task isn't fetched as well)
	 */
	fun isTaskDeepFetched(id: Long): Boolean {
		return false
	}

	suspend fun fetchByIdIfStale(
		id: Long,
		forceFetch: Boolean = false,
	): Result<TableTask> {
		val task = dataKeysById(id).firstOrNull()
		if (!forceFetch && task != null) {
			Result.success(task)
		}

		val retrievedData = api
			.getById(id)
			.onFailure {
				return Result.failure(it)
			}
			.getOrThrow()

		update(combineForUpdate(retrievedData))

		return Result.success(retrievedData)
	}

	/**
	 * NOTE [data].Key I.E [TableTask] will have all data reset to default except [TableTask.id].
	 * this is to prevent multiple sources of truth
	 * @param lastFetchTablesUpdated which tables should be notified that they have been updated
	 */
	fun update(
		data: Map<TableTask, Date>,
		vararg lastFetchTablesUpdated: Long,
	) {
		val taskDataOnly = data.mapKeys { entry ->
			entry.key.copy(
				childTasks = emptyList(),
				comments = emptyList(),
				labels = emptyList(),
				assigned = emptyList(),
			)
		}

		val lastFetches = state.value.lastFetchesByTableIds.toMutableMap()
		lastFetches.putAll(
			lastFetchTablesUpdated
				.toSet()
				.associateWith { defaultCacheValue() },
		)

		// TODO remove commend data and others and add them to repositories that they belong to

		mutableState.value = TableTaskRepositoryState(
			data = taskDataOnly,
			lastFetchesByTableIds = lastFetches,
		)

		val taskUpdatedIds = data.keys
			.map { it.id }
			.toLongArray()

		val comments = data.keys
			.flatMap { it.comments }
			.toTypedArray()
		TaskCommentRepository.update(
			data = TaskCommentRepository.combineForUpdate(*comments),
			lastFetchTasksUpdated = taskUpdatedIds,
		)

		val labels = data.keys
			.flatMap { it.labels }
			.toTypedArray()
		TaskLabelRepository.update(
			data = TaskLabelRepository.combineForUpdate(*labels),
			lastFetchTasksUpdated = taskUpdatedIds,
		)

		val assigned = data.keys
			.flatMap { it.assigned }
			.toTypedArray()
		TaskAssignedRepository.update(
			data = TaskAssignedRepository.combineForUpdate(*assigned),
			lastFetchTasksUpdated = taskUpdatedIds,
		)
	}

	override fun <T : Long> delete(vararg dataByIds: T) {
		val newData = state.value.data.filterKeys {
			!dataByIds.contains(it.getId())
		}

		val lastFetchesByTableIds = state.value.lastFetchesByTableIds.filterKeys { tableId ->
			newData.keys.any { it.tableId == tableId }
		}

		mutableState.value = state.value.copy(
			data = newData,
			lastFetchesByTableIds = lastFetchesByTableIds,
		)

		val commendIds = TaskCommentRepository
			.dataByTaskIds(*dataByIds.toLongArray())
			.map { it.key.id }
		TaskCommentRepository.delete(*commendIds.toTypedArray())

		val labelIds = TaskLabelRepository
			.dataByTaskIds(*dataByIds.toLongArray())
			.map { it.key.id }
		TaskLabelRepository.delete(*labelIds.toTypedArray())

		val assignedIds = TaskAssignedRepository
			.dataByTaskIds(*dataByIds.toLongArray())
			.map { it.key }
		TaskAssignedRepository.delete(*assignedIds.toTypedArray())
	}

	@Composable
	fun childTaskDataKeysCollectedById(id: Long): Set<TableTask> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id,
		) {
			stateCollected.data.keys
				.filter { it.parentTaskId == id }
				.toSet()
		}
	}

	@Composable
	fun dataKeysCollectedByTableId(tableId: Long): Set<TableTask> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			tableId,
		) {
			stateCollected.data.keys
				.filter { it.tableId == tableId }
				.toSet()
		}
	}

	fun dataByTableIds(vararg tableIds: Long): Map<TableTask, Date> {
		return data().filterKeys { tableIds.contains(it.tableId) }
	}
}
