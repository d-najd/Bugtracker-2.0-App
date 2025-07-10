package io.dnajd.data.project

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectListResponse
import io.dnajd.domain.project.service.ProjectRepository
import java.util.Date

object MockProjectRepository : ProjectRepository {
	override suspend fun getAll(): Result<ProjectListResponse> = Result.success(
		mockData()
	)

	override suspend fun getById(id: Long): Result<Project> = Result.success(mockData().data[0])

	override suspend fun createProject(project: Project): Result<Project> = Result.success(project)

	override suspend fun updateProject(project: Project): Result<Project> = Result.success(project)

	override suspend fun deleteById(id: Long): Result<Unit> = Result.success(Unit)

	private fun mockData() = ProjectListResponse(
		listOf(
			Project(
				1,
				"user1",
				"Example Title",
				null,
				Date()
			),
			Project(
				2,
				"user2",
				"Title 2",
				"Example Description",
				Date()
			)
		)
	)
}

