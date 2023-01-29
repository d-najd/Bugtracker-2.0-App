package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class GetProjectTableTask(
    private val repository: ProjectTableTaskRepository,
) {
    suspend fun awaitOne(taskId: Long): ProjectTableTask? = repository.get(taskId)
}
