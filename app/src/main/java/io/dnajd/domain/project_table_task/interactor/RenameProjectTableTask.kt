package io.dnajd.domain.project_table_task.interactor

import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository

class RenameProjectTableTask(
    private val repository: ProjectTableTaskRepository,
) {
    suspend fun await(id: Long, newTitle: String): Boolean = repository.updateNoBody(id = id, title = newTitle)
}
