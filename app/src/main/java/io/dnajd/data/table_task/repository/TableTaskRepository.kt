package io.dnajd.data.table_task.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
	RepositoryBase<TableTask, Date, TableTaskRepositoryState>(TableTaskRepositoryState()) {

	private val api: TableTaskApiService = Injekt.get()

	suspend fun fetchByIdIfStale(
		id: Long,
		forceFetch: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && data().any { it.key.id == id }) {
			Result.success(Unit)
		}

		val result = api.getById(id)
		val resultMapped = result.map { }
		if (result.isFailure) {
			return resultMapped
		}

		val newData = result.getOrThrow()
		val combinedData = combineForUpdate(newData)
		update(combinedData)

		return resultMapped
	}

	override fun defaultCacheValue(): Date {
		return Date()
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
				},
			)
		}

		val lastFetches = state.value.lastFetchesByTableIds.toMutableMap()
		lastFetches.putAll(
			lastFetchTablesUpdated
				.toSet()
				.associateWith { Date() },
		)

		mutableState.value = TableTaskRepositoryState(
			data = dataWithoutSubtaskData,
			lastFetchesByTableIds = lastFetches,
		)
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

	@Composable
	fun dataKeyCollectedById(id: Long): TableTask? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id,
		) {
			stateCollected.data.keys.firstOrNull { it.id == id }
		}
	}

	fun dataByTableId(tableId: Long): Map<TableTask, Date> {
		return data().filterKeys { it.tableId == tableId }
	}

	fun dataKeyById(id: Long): TableTask? {
		return state.value.data.keys.firstOrNull { it.id == id }
	}
}
