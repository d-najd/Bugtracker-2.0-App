package io.dnajd.domain.project.interactor

import io.dnajd.domain.project.service.ProjectRepository

class DeleteProject(
    private val repository: ProjectRepository,
) {
    suspend fun await(id: Long): Boolean = repository.delete(id)
}
