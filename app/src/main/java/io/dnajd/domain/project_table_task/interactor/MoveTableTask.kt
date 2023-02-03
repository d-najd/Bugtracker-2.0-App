package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class MoveTableTask(
    private val repository: ProjectTableTaskRepository,
){
    suspend fun await(fId: Long, sId: Long): Boolean = repository.movePositionTo(fId, sId)
}
