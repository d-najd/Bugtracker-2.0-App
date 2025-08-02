package io.dnajd.data.task_assigned.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.task_assigned.model.TaskAssigned
import java.util.Date

data class TaskAssignedRepositoryState(
	override val data: Map<TaskAssigned, Date> = emptyMap(),
	val lastFetchesByTaskIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TaskAssigned, Date>(data)

object TaskAssignedRepository :
	RepositoryBase<TaskAssigned, TaskAssigned, Date, TaskAssignedRepositoryState>(TaskAssignedRepositoryState()) {

	fun update(
		data: Map<TaskAssigned, Date>,
		vararg lastFetchTasksUpdated: Long,
	) {
		val lastFetches = state.value.lastFetchesByTaskIds.toMutableMap()

		lastFetches.putAll(
			lastFetchTasksUpdated
				.toSet()
				.associateWith { defaultCacheValue() },
		)

		mutableState.value = state.value.copy(
			data = data,
			lastFetchesByTaskIds = lastFetches,
		)
	}

	override fun <T : TaskAssigned> delete(vararg dataById: T) {
		val newData = state.value.data.filterKeys {
			!dataById.contains(it.getId())
		}

		val lastFetchedByTaskIds = state.value.lastFetchesByTaskIds.filterKeys { taskId ->
			newData.keys.any { it.taskId == taskId }
		}

		mutableState.value = state.value.copy(
			data = newData,
			lastFetchesByTaskIds = lastFetchedByTaskIds,
		)
	}

	@Composable
	fun dataKeysCollectedByTaskIds(vararg taskIds: Long): Set<TaskAssigned> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			taskIds,
		) {
			stateCollected.data.keys
				.filter { assigned -> taskIds.contains(assigned.taskId) }
				.toSet()
		}
	}

	fun dataByTaskIds(vararg taskIds: Long): Map<TaskAssigned, Date> {
		return data().filterKeys { assigned -> taskIds.contains(assigned.taskId) }
	}
}
