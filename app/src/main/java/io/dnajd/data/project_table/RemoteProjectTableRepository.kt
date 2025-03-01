package io.dnajd.data.project_table

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse
import io.dnajd.domain.project_table.service.ProjectTableRepository
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectTableRepository : ProjectTableRepository {
	private val factory: ProjectTableRepositoryApi =
		Injekt.get<Retrofit.Builder>()
			.baseUrl("${Urls.PROJECT_TABLE.getAppendedUrl()}/").build()
			.create(ProjectTableRepositoryApi::class.java)

	override suspend fun getAll(
		projectId: Long,
		ignoreTasks: Boolean,
	): Result<ProjectTableListResponse> =
		factory.getTablesByProjectId(projectId, ignoreTasks)

	override suspend fun getOne(
		id: Long,
		ignoreTasks: Boolean,
	): Result<ProjectTable> = factory.getById(id, ignoreTasks)

	override suspend fun create(table: ProjectTable): Result<ProjectTable> =
		factory.createTable(table)

	override suspend fun updateNoBody(
		id: Long,
		title: String?,
	): Result<Unit> = factory.updateNoBody(
		id = id,
		title = title,
	)

	override suspend fun swapPositionWith(fId: Long, sId: Long): Result<Unit> =
		factory.swapTablePositions(id = fId, sId = sId)

	override suspend fun delete(id: Long): Result<Unit> =
		factory.deleteTable(id)
}

private interface ProjectTableRepositoryApi {
	@GET("projectId/{projectId}")
	fun getTablesByProjectId(
		@Path("projectId") projectId: Long,
		@Query("ignoreIssues") ignoreTasks: Boolean,
	): Result<ProjectTableListResponse>

	@GET("id/{id}")
	fun getById(
		@Path("id") id: Long,
		@Query("ignoreIssues") ignoreTasks: Boolean,
	): Result<ProjectTable>

	@POST(Urls.PROJECT_TABLE.appendedUrlLocal)
	fun createTable(@Body table: ProjectTable): Result<ProjectTable>

	/**
	 * Do not modify [returnBody]
	 */
	@PUT("{id}")
	fun updateNoBody(
		@Path("id") id: Long,
		@Query("title") title: String? = null,
		@Query("returnBody") returnBody: Boolean = false,
	): Result<Unit>

	@PATCH("{id}/swapPositionWith/{sId}")
	fun swapTablePositions(
		@Path("id") id: Long,
		@Path("sId") sId: Long,
	): Result<Unit>

	@DELETE("{id}")
	fun deleteTable(@Path("id") id: Long): Result<Unit>
}