package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class SwapTableTaskTable(
	private val repository: ProjectTableTaskRepository,
){
	suspend fun await(id: Long, tableId: Long): Boolean = repository.swapTable(id = id, tableId = tableId)
}
