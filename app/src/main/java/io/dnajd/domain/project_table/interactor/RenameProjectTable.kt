package io.dnajd.domain.project_table.interactor

import io.dnajd.domain.project_table.service.ProjectTableRepository

class RenameProjectTable(
    private val repository: ProjectTableRepository,
) {
    suspend fun await(id: Long, newTitle: String): Boolean = repository.changeTitle(id, newTitle)
}
