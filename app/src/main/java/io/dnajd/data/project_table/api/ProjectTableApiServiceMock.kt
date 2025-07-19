package io.dnajd.data.project_table.api

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.ProjectLabel
import io.dnajd.domain.table_task.model.TableTask

object ProjectTableApiServiceMock : ProjectTableApiService {
	override suspend fun getAllByProjectId(
		projectId: Long,
		includeTasks: Boolean,
	): Result<ProjectTableListResponse> = Result.success(mockData(includeTasks))

	override suspend fun getById(
		id: Long,
		includeTasks: Boolean,
	): Result<ProjectTable> = Result.success(mockData(includeTasks).data[0])

	override suspend fun createTable(table: ProjectTable): Result<ProjectTable> =
		Result.success(table)

	override suspend fun updateTable(table: ProjectTable): Result<ProjectTable> =
		Result.success(table)

	override suspend fun swapTablePositions(
		fId: Long,
		sId: Long,
	): Result<Unit> = Result.success(Unit)

	override suspend fun deleteById(id: Long): Result<Unit> = Result.success(Unit)

	private fun mockData(includeTasks: Boolean) = ProjectTableListResponse(
		listOf(
			ProjectTable(
				id = 1,
				title = "Table 1",
				position = 0,
				tasks = if (includeTasks) listOf(
					TableTask(
						1,
						title = "Issue 1",
						tableId = 1,
						parentTaskId = null,
						severity = 1,
						position = 0,
						labels = listOf(
							ProjectLabel(
								id = 1,
								label = "LABEL 1",
							),
							ProjectLabel(
								id = 2,
								label = "LABEL 2",
							),
						),
						childTasks = listOf(
							TableTask(id = 2),
							TableTask(id = 4),
						),
					),
					TableTask(
						id = 2,
						title = "Issue 2",
						tableId = 1,
						parentTaskId = 1,
						severity = 3,
						position = 1,
						labels = emptyList(),
						childTasks = listOf(
							TableTask(id = 3),
						)
					),
					TableTask(
						id = 3,
						title = "Issue 3",
						tableId = 1,
						parentTaskId = 2,
						severity = 3,
						position = 2,
						labels = emptyList(),
						childTasks = emptyList(),
					),
					TableTask(
						id = 4,
						title = "Issue 4",
						tableId = 1,
						parentTaskId = 2,
						severity = 3,
						position = 3,
						labels = emptyList(),
						childTasks = emptyList(),
					),
				) else emptyList()
			),
			ProjectTable(
				id = 2,
				title = "Table 2",
				position = 1,
				tasks = if (!includeTasks) listOf(
					TableTask(
						id = 5,
						title = "Issue 5",
						tableId = 1,
						parentTaskId = 1,
						severity = 2,
						position = 0,
						labels = emptyList(),
						childTasks = emptyList(),
					),
				) else emptyList(),
			),
			ProjectTable(
				id = 3,
				title = "Table 3",
				position = 2,
				tasks = emptyList(),
			),
		)
	)
}

