package io.dnajd.data.project_table

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableRepository

object FakeProjectTableRepository : ProjectTableRepository {

    override suspend fun getProjectTables(projectId: Long): List<ProjectTable> = listOf(
        ProjectTable(1, 1, "Title 1", 0),
        ProjectTable(2, 1, "Title 2", 1),
        ProjectTable(3, 1, "Title 3", 2),
    )

}

