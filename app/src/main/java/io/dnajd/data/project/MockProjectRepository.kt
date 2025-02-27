package io.dnajd.data.project

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectHolder
import io.dnajd.domain.project.service.ProjectRepository
import java.util.Date

object MockProjectRepository : ProjectRepository {
	override suspend fun getAll(username: String): Result<ProjectHolder> = Result.success(
		mockData()
	)

	override suspend fun get(id: Long): Result<Project> =
		Result.success(mockData().data[0])

	override suspend fun create(project: Project): Result<Project> = Result.success(project)

	override suspend fun updateNoBody(
		id: Long,
		title: String?,
		description: String?
	): Result<Unit> = Result.success(Unit)

	override suspend fun delete(id: Long): Result<Unit> = Result.success(Unit)

	private fun mockData() = ProjectHolder(
		listOf(
			Project(1, "user1", "Example Title", null, Date()),
			Project(2, "user2", "Title 2", "Example Description", Date())
		)
	)
}

