package io.dnajd.domain.project.interactor

import io.dnajd.domain.project.service.ProjectRepository

class RenameProject(
    private val repository: ProjectRepository,
) {
    suspend fun await(id: Long, newTitle: String): Boolean = repository.changeTitle(id, newTitle)
}
