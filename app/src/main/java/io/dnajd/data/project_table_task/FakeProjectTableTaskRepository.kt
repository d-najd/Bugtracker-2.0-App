package io.dnajd.data.project_table_task

import io.dnajd.domain.project_table_task.model.*
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository
import java.util.*

object FakeProjectTableTaskRepository : ProjectTableTaskRepository {

    @Suppress("RedundantNullableReturnType")
    override suspend fun get(taskId: Long): ProjectTableTask? = ProjectTableTask(
        id = 1L,
        title = "Example Title",
        tableId = 1L,
        parentTaskId = 2L,
        severity = 3,
        position = 0,
        labels = listOf(
            ProjectLabel(1L, "Label 1"),
            ProjectLabel(2L, "Label 2"),
            ProjectLabel(3L, "Label 3")
        ),
        childTasks = listOf(
            ProjectTableChildTask(
                id = 3L,
                title = "Child Task 1",
                tableId = 1L,
            ),
            ProjectTableChildTask(
                id = 4L,
                title = "Child Task 2",
                tableId = 2L,
            )
        ),
        reporter = "user1",
        description = "This is an example description",
        createdAt = Date(),
        updatedAt = null,
        assigned = listOf(
            ProjectTableIssueAssigne(
                assignedUsername = "user1",
                assignerUsername = "user2",
            ),
            ProjectTableIssueAssigne(
                assignedUsername = "user1",
                assignerUsername = "user1",
            )
        ),
        comments = listOf(
            ProjectTableTaskComment(
                id = 1L,
                user = "user1",
                message ="This is a basic comment",
                createdAt = Date(),
                editedAt = null,
            ),
            ProjectTableTaskComment(
                id = 2L,
                user = "user1",
                message = "This is a edited comment",
                createdAt = Date(),
                editedAt = Date(),
            ),
            ProjectTableTaskComment(
                id = 3L,
                user = "user2",
                message = "This is a edited comment by another user",
                createdAt = Date(),
                editedAt = Date(),
            )
        )
    )

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean = true

    override suspend fun movePositionTo(fId: Long, sId: Long): Boolean = true

}
