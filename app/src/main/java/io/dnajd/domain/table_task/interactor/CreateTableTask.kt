package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository

class CreateTableTask(
	private val repository: TableTaskRepository,
) {
	suspend fun awaitOne(task: TableTask): TableTask? = repository.create(task)
}
