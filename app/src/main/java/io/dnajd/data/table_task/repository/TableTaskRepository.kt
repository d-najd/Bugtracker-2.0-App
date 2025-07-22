package io.dnajd.data.table_task.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTask
import java.util.Date

data class TableTaskRepositoryState(
	override val data: Map<TableTask, Date> = emptyMap(),
	val lastFetchesByTableIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TableTask, Date>(data)

object TableTaskRepository :
	RepositoryBase<TableTask, Date, TableTaskRepositoryState>(TableTaskRepositoryState()) {

	@Composable
	fun dataKeysCollectedByTableId(tableId: Long): Set<TableTask> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			tableId
		) {
			stateCollected.data.keys
				.filter { it.tableId == tableId }
				.toSet()
		}
	}

	fun dataByTableId(tableId: Long): Map<TableTask, Date> {
		return data().filterKeys { it.tableId == tableId }
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

		// Only the id's are kept, fetch the other data manually, this is to avoid multiple sources
		// of truth
		val dataWithoutSubtaskData = data.mapKeys { entry ->
			entry.key.copy(
				childTasks = entry.key.childTasks.map { childTask ->
					TableTask(id = childTask.id)
				})
		}

		val lastFetches = state.value.lastFetchesByTableIds.toMutableMap()
		lastFetches.putAll(
			lastFetchTablesUpdated
				.toSet()
				.associateWith { Date() })

		mutableState.value = TableTaskRepositoryState(
			data = dataWithoutSubtaskData,
			lastFetchesByTableIds = lastFetches
		)
	}
}