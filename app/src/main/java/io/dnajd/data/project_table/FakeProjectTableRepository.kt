package io.dnajd.data.project_table

import io.dnajd.domain.project_table_task.model.ProjectLabel
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.model.ProjectTableChildTask
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table.service.ProjectTableRepository

object FakeProjectTableRepository : ProjectTableRepository {
    override suspend fun getAll(projectId: Long): List<ProjectTable> = listOf(
        ProjectTable(
            id = 1,
            title = "Table 1",
            position = 0,
            tasks = listOf(
                ProjectTableTask(
                    id = 1,
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
                        ProjectTableChildTask(id = 2),
                        ProjectTableChildTask(id = 4),
                    ),
                ),
                ProjectTableTask(
                    id = 2,
                    title = "Issue 2",
                    tableId = 1,
                    parentTaskId = 1,
                    severity = 3,
                    position = 1,
                    labels = emptyList(),
                    childTasks = listOf(
                        ProjectTableChildTask(id = 3),
                    )
                ),
                ProjectTableTask(
                    id = 3,
                    title = "Issue 3",
                    tableId = 1,
                    parentTaskId = 2,
                    severity = 3,
                    position = 2,
                    labels = emptyList(),
                    childTasks = emptyList(),
                ),
                ProjectTableTask(
                    id = 4,
                    title = "Issue 4",
                    tableId = 1,
                    parentTaskId = 2,
                    severity = 3,
                    position = 3,
                    labels = emptyList(),
                    childTasks = emptyList(),
                ),
            )
        ),
        ProjectTable(
            id = 2,
            title = "Table 2",
            position = 1,
            tasks = listOf(
                ProjectTableTask(
                    id = 5,
                    title = "Issue 5",
                    tableId = 1,
                    parentTaskId = 1,
                    severity = 2,
                    position = 0,
                    labels = emptyList(),
                    childTasks = emptyList(),
                ),
            ),
        ),
        ProjectTable(
            id = 3,
            title = "Table 3",
            position = 2,
            tasks = emptyList(),
        ),
    )

    @Suppress("RedundantNullableReturnType")
    override suspend fun changeTitle(id: Long, newTitle: String): Boolean = true

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean = true

}

