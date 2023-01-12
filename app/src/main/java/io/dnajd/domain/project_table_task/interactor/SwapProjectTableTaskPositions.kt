package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class SwapProjectTableTaskPositions(
    private val repository: ProjectTableTaskRepository,
){
    suspend fun await(fId: Long, sId: Long): Boolean {
        return repository.swapTaskPositions(fId, sId)
    }
}
