package io.dnajd.data.table_task.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTask
import java.util.Date

data class TableTaskRepositoryState(
	override val data: Map<TableTask, Date> = emptyMap(),
	val lastFetchByTableId: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<TableTask, Date>(data)

object TableTaskRepository :
	RepositoryBase<TableTask, Date, TableTaskRepositoryState>(TableTaskRepositoryState()) {

	/**
	 * NOTE [data].Key I.E [TableTask] will have all data reset to default except [TableTask.id].
	 * this is to prevent multiple sources of truth
	 * @param lastFetchTablesUpdated which tables should be notified that they have been updated
	 */
	fun update(
		data: Map<TableTask, Date>,
		vararg lastFetchTablesUpdated: Long,
	) {		// Only the id's are kept, fetch the other data manually, this is to avoid multiple sources
		// of truth
		val dataWithoutSubtaskData = data.mapKeys {
			TableTask(id = it.key.id)
		}

		val lastFetches = mutableState.value.lastFetchByTableId.mapValues {
			if (lastFetchTablesUpdated.contains(it.key)) Date() else it.value
		}

		mutableState.value = TableTaskRepositoryState(
			data = dataWithoutSubtaskData,
			lastFetchByTableId = lastFetches
		)
	}
}