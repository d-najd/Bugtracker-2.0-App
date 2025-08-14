package io.dnajd.data.task_comment.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.task_comment.model.TaskComment
import io.dnajd.domain.task_comment.service.TaskCommentApiService
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object TaskCommentApiServiceImpl : TaskCommentApiService {
	private val factory: TaskCommentApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.TASK_COMMENT)
		.build()
		.create(TaskCommentApi::class.java)

	override suspend fun create(
		taskId: Long,
		comment: TaskComment,
	): Result<TaskComment> = factory.create(taskId, comment)

	override suspend fun update(comment: TaskComment): Result<TaskComment> =
		factory.update(comment.id, comment)

	override suspend fun delete(id: Long): Result<Unit> = factory.delete(id)
}

private interface TaskCommentApi {
	@POST("issueId/{taskId}")
	suspend fun create(
		@Path("taskId") taskId: Long,
		@Body comment: TaskComment,
	): Result<TaskComment>

	@PUT("{id}")
	suspend fun update(
		@Path("id") id: Long,
		@Body comment: TaskComment,
	): Result<TaskComment>

	@DELETE("{id}")
	suspend fun delete(@Path("id") id: Long): Result<Unit>
}
