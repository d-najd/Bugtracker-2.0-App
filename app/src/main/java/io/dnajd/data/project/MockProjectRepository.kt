package io.dnajd.data.project

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import java.util.Date

object MockProjectRepository : ProjectRepository {

	override suspend fun getAll(username: String): List<Project> = listOf(
		Project(1, "user1", "Example Title", null, Date()),
		Project(2, "user2", "Title 2", "Example Description", Date())
	)

	@Suppress("RedundantNullableReturnType")
	override suspend fun get(id: Long): Project? =
		Project(1, "user1", "Example Title", null, Date())

	@Suppress("RedundantNullableReturnType")
	override suspend fun create(project: Project): Project? = project

	override suspend fun updateNoBody(
		id: Long,
		title: String?,
		description: String?
	): Boolean = true

	override suspend fun delete(id: Long): Boolean = true

}

