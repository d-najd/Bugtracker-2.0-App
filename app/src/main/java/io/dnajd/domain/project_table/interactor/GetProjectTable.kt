package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository

class GetProjectTable(
    private val repository: ProjectTableRepository,
) {
    suspend fun await(projectId: Long, ignoreTasks: Boolean = false): List<ProjectTable> = repository.getAll(projectId, ignoreTasks)

    suspend fun awaitOne(id: Long, ignoreTasks: Boolean = false): ProjectTable? = repository.getOne(id, ignoreTasks)
}