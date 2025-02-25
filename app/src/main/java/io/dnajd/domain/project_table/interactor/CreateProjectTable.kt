package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.service.ProjectTableRepository

class DeleteProjectTable(
	private val repository: ProjectTableRepository,
) {
	suspend fun awaitOne(id: Long): Boolean = repository.delete(id)
}
