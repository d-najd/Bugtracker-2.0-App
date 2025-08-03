package io.dnajd.domain.task_comment.service

import io.dnajd.domain.task_comment.model.TaskComment

interface TaskCommentApiService {
	suspend fun create(
		taskId: Long,
		comment: TaskComment,
	): Result<TaskComment>

	suspend fun update(
		comment: TaskComment,
	): Result<TaskComment>

	suspend fun delete(
		id: Long,
	): Result<Unit>
}
