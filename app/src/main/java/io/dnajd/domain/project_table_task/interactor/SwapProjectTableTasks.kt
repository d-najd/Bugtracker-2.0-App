package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class SwapProjectTableTasks(
    private val repository: ProjectTableTaskRepository,
){
    suspend fun await(fId: Long, sId: Long): Boolean {
        return repository.swapPositionWith(fId, sId)
    }
}
