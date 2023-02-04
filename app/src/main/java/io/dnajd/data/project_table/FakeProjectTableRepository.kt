package io.dnajd.data.project_table

import io.dnajd.domain.table_task.model.ProjectLabel
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.table_task.model.ProjectTableChildTaskBasic
import io.dnajd.domain.table_task.model.TableTaskBasic
import io.dnajd.domain.project_table.service.ProjectTableRepository

object FakeProjectTableRepository : ProjectTableRepository {

    override suspend fun getAll(
        projectId: Long,
        ignoreTasks: Boolean
    ): List<ProjectTable> = listOf(
        ProjectTable(
            id = 1,
            title = "Table 1",
            position = 0,
            tasks = if (!ignoreTasks) listOf(
                TableTaskBasic(
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
                        ProjectTableChildTaskBasic(id = 2),
                        ProjectTableChildTaskBasic(id = 4),
                    ),
                ),
                TableTaskBasic(
                    id = 2,
                    title = "Issue 2",
                    tableId = 1,
                    parentTaskId = 1,
                    severity = 3,
                    position = 1,
                    labels = emptyList(),
                    childTasks = listOf(
                        ProjectTableChildTaskBasic(id = 3),
                    )
                ),
                TableTaskBasic(
                    id = 3,
                    title = "Issue 3",
                    tableId = 1,
                    parentTaskId = 2,
                    severity = 3,
                    position = 2,
                    labels = emptyList(),
                    childTasks = emptyList(),
                ),
                TableTaskBasic(
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
            tasks = if (!ignoreTasks) listOf(
                TableTaskBasic(
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

    override suspend fun getOne(
        id: Long,
        ignoreTasks: Boolean
    ) = ProjectTable(
        id = 1,
        title = "Table 1",
        position = 0,
        tasks = if (!ignoreTasks) listOf(
            TableTaskBasic(
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
                    ProjectTableChildTaskBasic(id = 2),
                    ProjectTableChildTaskBasic(id = 4),
                ),
            ),
            TableTaskBasic(
                id = 2,
                title = "Issue 2",
                tableId = 1,
                parentTaskId = 1,
                severity = 3,
                position = 1,
                labels = emptyList(),
                childTasks = listOf(
                    ProjectTableChildTaskBasic(id = 3),
                )
            ),
            TableTaskBasic(
                id = 3,
                title = "Issue 3",
                tableId = 1,
                parentTaskId = 2,
                severity = 3,
                position = 2,
                labels = emptyList(),
                childTasks = emptyList(),
            ),
            TableTaskBasic(
                id = 4,
                title = "Issue 4",
                tableId = 1,
                parentTaskId = 2,
                severity = 3,
                position = 3,
                labels = emptyList(),
                childTasks = emptyList(),
            ),
        ) else emptyList(),
    )

    @Suppress("RedundantNullableReturnType")
    override suspend fun create(table: ProjectTable): ProjectTable? = table

    override suspend fun updateNoBody(
        id: Long,
        title: String?
    ): Boolean = true

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean = true

    override suspend fun delete(id: Long): Boolean = true

}

