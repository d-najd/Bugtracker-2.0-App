package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.service.TableTaskRepository

class SwapTableTaskTable(
	private val repository: TableTaskRepository,
){
	suspend fun await(id: Long, tableId: Long): Boolean = repository.swapTable(id = id, tableId = tableId)
}
