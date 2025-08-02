package io.dnajd.domain.task_comment.service

import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.task_comment.model.TaskComment

interface TaskCommentService {
	suspend fun create(
		taskId: Long,
		comment: TaskComment,
	): Result<TaskComment>

	suspend fun update(
		comment: TaskComment,
	): Result<TableTask>

	suspend fun delete(
		id: Long,
	): Result<Unit>
}
