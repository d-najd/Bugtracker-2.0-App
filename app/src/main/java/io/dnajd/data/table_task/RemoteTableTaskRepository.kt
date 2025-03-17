package io.dnajd.data.table_task

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.toResult
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository
import retrofit2.Call
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
			.baseUrl(Urls.PROJECT_TABLE_ISSUE).build()
			.create(TableTaskRepositoryApi::class.java)

	override suspend fun getById(id: Long): Result<TableTask> =
		factory.getById(id).toResult()

	override suspend fun createTask(task: TableTask): Result<TableTask> =
		factory.createTask(task).toResult()

	override suspend fun updateTask(task: TableTask): Result<TableTask> =
		factory.updateTask(task.id, task).toResult()

	override suspend fun swapTaskPositions(fId: Long, sId: Long): Result<Unit> =
		factory.swapTaskPositions(fId = fId, sId = sId).toResult()

	override suspend fun movePositionTo(fId: Long, sId: Long): Result<Unit> =
		factory.moveTaskPositions(fId = fId, sId = sId).toResult()

	override suspend fun swapTable(id: Long, tableId: Long): Result<Unit> =
		factory.swapTable(id = id, tableId = tableId).toResult()
}

private interface TableTaskRepositoryApi {
	@GET("{id}")
	fun getById(
		@Path("id") id: Long,
		@Query("includeChildIssues") includeChildTasks: Boolean = true,
		@Query("includeAssigned") includeAssigned: Boolean = true,
		@Query("includeComments") includeComments: Boolean = true,
		@Query("includeLabels") includeLabels: Boolean = true,
	): Call<TableTask>

	@POST
	fun createTask(
		@Body task: TableTask,
	): Call<TableTask>

	@PUT("{id}")
	fun updateTask(
		@Path("id") id: Long,
		@Body task: TableTask,
	): Call<TableTask>

	@PATCH("{fId}/swapPositionWith/{sId}")
	fun swapTaskPositions(
		@Path("fId") fId: Long,
		@Path("sId") sId: Long,
	): Call<Unit>

	@PATCH("{fId}/movePositionTo/{sId}")
	fun moveTaskPositions(
		@Path("fId") fId: Long,
		@Path("sId") sId: Long,
	): Call<Unit>

	@PATCH("{id}/swapTable/{tableId}")
	fun swapTable(
		@Path("id") id: Long,
		@Path("tableId") tableId: Long,
	): Call<Unit>
}
