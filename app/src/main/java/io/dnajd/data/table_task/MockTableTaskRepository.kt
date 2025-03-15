package io.dnajd.data.table_task

import io.dnajd.domain.table_task.model.ProjectLabel
import io.dnajd.domain.table_task.model.TableChildTask
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.model.TableTaskAssignee
import io.dnajd.domain.table_task.model.TableTaskComment
import io.dnajd.domain.table_task.service.TableTaskRepository
import java.util.Date

object MockTableTaskRepository : TableTaskRepository {

	override suspend fun getById(id: Long): Result<TableTask> = Result.success(mockData())

	override suspend fun createTask(task: TableTask): Result<TableTask> = Result.success(task)

	override suspend fun updateTask(task: TableTask): Result<TableTask> = Result.success(task)

	override suspend fun swapTaskPositions(fId: Long, sId: Long): Result<Unit> =
		Result.success(Unit)

	override suspend fun movePositionTo(fId: Long, sId: Long): Result<Unit> = Result.success(Unit)

	override suspend fun swapTable(id: Long, tableId: Long): Result<Unit> = Result.success(Unit)

	private fun mockData() = TableTask(
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
			TableChildTask(
				id = 3L,
				title = "Child Task 1",
				tableId = 1L,
			),
			TableChildTask(
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
			TableTaskAssignee(
				assignedUsername = "user1",
				assignerUsername = "user2",
			),
			TableTaskAssignee(
				assignedUsername = "user1",
				assignerUsername = "user1",
			)
		),
		comments = listOf(
			TableTaskComment(
				id = 1L,
				user = "user1",
				message = "This is a basic comment",
				createdAt = Date(Date().time - 3600000),
				editedAt = null,
			),
			TableTaskComment(
				id = 2L,
				user = "user1",
				message = "This is a edited comment",
				createdAt = Date(Date().time - 3600000),
				editedAt = Date(Date().time - 1800000),
			),
			TableTaskComment(
				id = 3L,
				user = "user2",
				message = "This is a edited comment by another user",
				createdAt = Date(Date().time - 1800000),
				editedAt = Date(Date().time - 900000),
			)
		)
	)
}
