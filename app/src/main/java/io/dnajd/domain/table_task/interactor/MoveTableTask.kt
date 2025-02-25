package io.dnajd.domain.table_task.interactor

import io.dnajd.domain.table_task.service.TableTaskRepository

class MoveTableTask(
	private val repository: TableTaskRepository,
) {
	suspend fun await(fId: Long, sId: Long): Boolean = repository.movePositionTo(fId, sId)
}
