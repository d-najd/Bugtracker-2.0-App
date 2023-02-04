package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.service.TableTaskRepository

class UpdateTableTaskDescription(
	private val repository: TableTaskRepository,
) {
	suspend fun await(id: Long, description: String): Boolean = repository.updateNoBody(id = id, description = description)
}
