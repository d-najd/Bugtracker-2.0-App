package io.dnajd.domain.project_table.service

import io.dnajd.domain.project_table.model.ProjectTable

interface ProjectTableRepository {

    suspend fun getProjectTables(projectId: Long): List<ProjectTable>

}