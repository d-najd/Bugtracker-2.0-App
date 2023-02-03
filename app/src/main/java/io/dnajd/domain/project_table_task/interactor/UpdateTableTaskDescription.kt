package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class UpdateTableTaskDescription(
	private val repository: ProjectTableTaskRepository,
) {
	suspend fun await(id: Long, description: String): Boolean = repository.updateNoBody(id = id, description = description)
}
