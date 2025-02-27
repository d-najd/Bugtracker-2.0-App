package io.dnajd.domain.project.interactor

import io.dnajd.domain.project.service.ProjectRepository

class GetProject(
	private val repository: ProjectRepository,
) {
	// suspend fun await(username: String): List<Project> = repository.getAll(username)

	// suspend fun awaitOne(id: Long): Result<Project> = repository.get(id)
}