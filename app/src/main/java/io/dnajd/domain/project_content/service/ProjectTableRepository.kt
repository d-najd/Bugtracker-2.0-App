package io.dnajd.domain.project_content.service

import io.dnajd.domain.project_content.model.ProjectTable

interface ProjectTableRepository {

    suspend fun getProjectTables(projectId: Long): List<ProjectTable>

}