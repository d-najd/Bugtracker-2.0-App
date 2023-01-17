package io.dnajd.domain.project.interactor

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository

class GetProjects(
    private val repository: ProjectRepository,
) {
    suspend fun await(username: String): List<Project> = repository.getAll(username)
}