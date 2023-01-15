package io.dnajd.data.project_table_task

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

object FakeProjectTableTaskRepository : ProjectTableTaskRepository {

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean = true

    override suspend fun movePositionTo(fId: Long, sId: Long): Boolean = true

}
