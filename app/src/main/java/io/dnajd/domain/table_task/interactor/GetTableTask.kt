package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository

class GetTableTask(
	private val repository: TableTaskRepository,
) {
	suspend fun awaitOne(taskId: Long): TableTask? = repository.get(taskId)
}
