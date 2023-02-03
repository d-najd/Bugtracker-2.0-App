package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class SwapTableTasks(
    private val repository: ProjectTableTaskRepository,
){
    suspend fun await(fId: Long, sId: Long): Boolean = repository.swapPositionWith(fId, sId)
}
