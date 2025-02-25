package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.service.TableTaskRepository

class RenameTableTask(
	private val repository: TableTaskRepository,
) {
	suspend fun await(id: Long, newTitle: String): Boolean =
		repository.updateNoBody(id = id, title = newTitle)
}
