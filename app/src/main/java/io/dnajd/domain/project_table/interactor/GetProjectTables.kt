package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository

class GetProjectTables(
    private val repository: ProjectTableRepository,
) {
    suspend fun await(projectId: Long): List<ProjectTable> {
        return repository.getProjectTables(projectId)
    }
}