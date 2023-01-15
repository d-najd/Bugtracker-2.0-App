package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class GetProjectTableTasks(
    private val repository: ProjectTableTaskRepository,
) {
    suspend fun awaitOne(taskId: Long): ProjectTableTask? {
        return repository.get(taskId)
    }
}
