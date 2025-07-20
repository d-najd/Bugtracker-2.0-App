package io.dnajd.data.table_task.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTaskBasic
import java.util.Date

data class TableTaskRepositoryState(
	override val data: Map<TableTaskBasic, Date?> = emptyMap(),
	val lastFetchByTableId: Map<Long, Date?> = emptyMap(),
) : RepositoryBase.State<TableTaskBasic>(data)

object TableTaskRepository :
	RepositoryBase<TableTaskBasic, TableTaskRepositoryState>(TableTaskRepositoryState()) {

	/**
	 * @param lastFetchTablesUpdated which tables should be notified that they have been updated
	 */
	fun update(
		data: Set<TableTaskBasic>,
		vararg lastFetchTablesUpdated: Long,
	) {
		val lastFetches = mutableState.value.lastFetchByTableId.mapValues {
			if (lastFetchTablesUpdated.contains(it.key)) Date() else it.value
		}

		mutableState.value = TableTaskRepositoryState(
			data = data.associateWith { Date() },
			lastFetchByTableId = lastFetches
		)
	}
}