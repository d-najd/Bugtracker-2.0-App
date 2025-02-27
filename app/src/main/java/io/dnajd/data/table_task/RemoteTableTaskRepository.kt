package io.dnajd.data.table_task

import io.dnajd.data.utils.Urls
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteTableTaskRepository : TableTaskRepository {
	private val factory: TableTaskRepositoryApi =
		Injekt.get<Retrofit.Builder>()
			.baseUrl("${Urls.TABLE_TASK.getAppendedUrl()}/").build()
			.create(TableTaskRepositoryApi::class.java)

	override suspend fun get(taskId: Long): Result<TableTask> =
		factory.get(taskId)

	override suspend fun create(task: TableTask): Result<TableTask> =
		factory.create(task)

	override suspend fun updateNoBody(
		id: Long,
		title: String?,
		description: String?,
		severity: Int?
	): Result<Unit> = factory.updateNoBody(
		id = id,
		title = title,
		description = description,
		severity = severity
	)

	override suspend fun swapPositionWith(fId: Long, sId: Long): Result<Unit> =
		factory.swapTaskPositions(id = fId, sId = sId)

	override suspend fun movePositionTo(fId: Long, sId: Long): Result<Unit> =
		factory.moveTaskPositions(id = fId, sId = sId)

	override suspend fun swapTable(id: Long, tableId: Long): Result<Unit> =
		factory.swapTable(id = id, tableId = tableId)
}

private interface TableTaskRepositoryApi {

	@GET("{id}")
	fun get(
		@Path("id") id: Long
	): Result<TableTask>

	@POST(Urls.TABLE_TASK.appendedUrlLocal)
	fun create(
		@Body task: TableTask,
	): Result<TableTask>

	/**
	 * Do not modify [returnBody]
	 */
	@PUT("{id}")
	fun updateNoBody(
		@Path("id") id: Long,
		@Query("title") title: String? = null,
		@Query("description") description: String? = null,
		@Query("severity") severity: Int? = null,
		@Query("returnBody") returnBody: Boolean = false,
	): Result<Unit>

	@PATCH("{id}/swapPositionWith/{sId}")
	fun swapTaskPositions(
		@Path("id") id: Long,
		@Path("sId") sId: Long,
	): Result<Unit>

	@PATCH("{id}/movePositionTo/{sId}")
	fun moveTaskPositions(
		@Path("id") id: Long,
		@Path("sId") sId: Long,
	): Result<Unit>

	@PATCH("{id}/swapTable/{tableId}")
	fun swapTable(
		@Path("id") id: Long,
		@Path("tableId") tableId: Long,
	): Result<Unit>

}
