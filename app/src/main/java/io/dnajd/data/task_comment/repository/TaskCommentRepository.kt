package io.dnajd.data.task_comment.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.task_comment.model.TaskComment
import java.util.Date

data class TaskCommentRepositoryState(
	override val data: Map<TaskComment, Date> = emptyMap(),
	val lastFetchesByTaskIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TaskComment, Date>(data)

object TaskCommentRepository :
	RepositoryBase<TaskComment, Long, Date, TaskCommentRepositoryState>(TaskCommentRepositoryState()) {

	fun update(
		data: Map<TaskComment, Date>,
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
	fun dataKeysCollectedByTaskIds(vararg taskIds: Long): Set<TaskComment> {
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

	fun dataByTaskIds(vararg taskIds: Long): Map<TaskComment, Date> {
		return data().filterKeys { taskIds.contains(it.taskId) }
	}
}
