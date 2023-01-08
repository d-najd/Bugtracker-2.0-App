package io.dnajd.domain.project_content.interactor

import io.dnajd.domain.project_content.model.ProjectTable
import io.dnajd.domain.project_content.service.ProjectTableRepository

class GetProjectTables(
    private val repository: ProjectTableRepository,
) {
    suspend fun await(projectId: Long): List<ProjectTable> {
        return repository.getProjectTables(projectId)
    }
}