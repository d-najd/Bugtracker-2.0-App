package io.dnajd.domain.project.interactor

import io.dnajd.domain.project.service.ProjectRepository

class CreateProject(
	private val repository: ProjectRepository,
) {
	// suspend fun awaitOne(project: Project): Project? = repository.create(project)
}
