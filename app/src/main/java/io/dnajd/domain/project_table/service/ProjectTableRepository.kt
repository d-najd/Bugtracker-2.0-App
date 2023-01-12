package io.dnajd.domain.project_table.service

import io.dnajd.domain.project_table.model.ProjectTable

interface ProjectTableRepository {

    suspend fun getTables(projectId: Long): List<ProjectTable>

    suspend fun renameTable(id: Long, newTitle: String): Boolean

}