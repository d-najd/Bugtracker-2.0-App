package io.dnajd.domain.task_comment.service

import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.task_comment.model.TableTaskComment

interface TaskCommentService {
	suspend fun create(
		taskId: Long,
		comment: TableTaskComment,
	): Result<TableTaskComment>

	suspend fun update(
		comment: TableTaskComment,
	): Result<TableTask>

	suspend fun delete(
		id: Long,
	): Result<Unit>
}
