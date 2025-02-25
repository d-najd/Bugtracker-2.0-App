package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository

class CreateProjectTable(
	private val repository: ProjectTableRepository,
) {
	suspend fun awaitOne(table: ProjectTable): ProjectTable? = repository.create(table)
}
