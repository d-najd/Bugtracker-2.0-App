package io.dnajd.data.project

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectRepository
import java.util.*

object FakeProjectRepository : ProjectRepository {

    override suspend fun getAll(username: String): List<Project> = listOf(
        Project(1, "user1", "Example Title", null, Date()),
        Project(2, "user2", "Title 2", "Example Description", Date())
    )

}

