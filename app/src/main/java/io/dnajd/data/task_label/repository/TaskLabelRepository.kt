package io.dnajd.data.task_label.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.task_label.model.TaskLabel
import java.util.Date

data class TaskLabelRepositoryState(
	override val data: Map<TaskLabel, Date> = emptyMap(),
	val lastFetchesByTaskIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TaskLabel, Date>(data)

object TaskLabelRepository :
	RepositoryBase<TaskLabel, Long, Date, TaskLabelRepositoryState>(TaskLabelRepositoryState()) {

	fun update(
		data: Map<TaskLabel, Date>,
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

	override fun <T : Long> delete(vararg dataById: T) {
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
	fun dataKeysCollectedByTaskIds(vararg taskIds: Long): Set<TaskLabel> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			taskIds,
		) {
			stateCollected.data.keys
				.filter { taskIds.contains(it.taskId) }
				.toSet()
		}
	}

	fun dataByTaskIds(vararg taskIds: Long): Map<TaskLabel, Date> {
		return data().filterKeys { taskIds.contains(it.taskId) }
	}
}
