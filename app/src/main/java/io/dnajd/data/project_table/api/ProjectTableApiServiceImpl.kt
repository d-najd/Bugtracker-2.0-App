package io.dnajd.data.project_table.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse
import io.dnajd.domain.project_table.service.ProjectTableApiService
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

object ProjectTableApiServiceImpl : ProjectTableApiService {
	private val factory: ProjectTableRepositoryApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.PROJECT_TABLE)
		.build()
		.create(ProjectTableRepositoryApi::class.java)

	override suspend fun getAllByProjectId(
		projectId: Long,
		includeTasks: Boolean,
	): Result<ProjectTableListResponse> = factory.getAllByProjectId(
		projectId,
		includeTasks,
	)

	override suspend fun getById(
		id: Long,
		includeTasks: Boolean,
	): Result<ProjectTable> = factory.getById(
		id,
		includeTasks,
	)

	override suspend fun createTable(table: ProjectTable): Result<ProjectTable> = factory.createTable(
		table.projectId,
		table,
	)

	override suspend fun updateTable(
		table: ProjectTable,
	): Result<ProjectTable> = factory.updateTable(
		table.id,
		table,
	)

	override suspend fun swapTablePositions(
		fId: Long,
		sId: Long,
	): Result<Unit> = factory.swapTablePositions(
		fId,
		sId,
	)

	override suspend fun deleteById(id: Long): Result<Unit> = factory.deleteById(id)
}

private interface ProjectTableRepositoryApi {
	@GET("projectId/{projectId}")
	suspend fun getAllByProjectId(
		@Path("projectId") projectId: Long,
		@Query("includeIssues") includeTasks: Boolean,
	): Result<ProjectTableListResponse>

	@GET("{id}")
	suspend fun getById(
		@Path("id") id: Long,
		@Query("includeIssues") includeTasks: Boolean,
	): Result<ProjectTable>

	@POST("projectId/{projectId}")
	suspend fun createTable(
		@Path("projectId") projectId: Long,
		@Body table: ProjectTable,
	): Result<ProjectTable>

	@PUT("{id}")
	suspend fun updateTable(
		@Path("id") id: Long,
		@Body table: ProjectTable,
	): Result<ProjectTable>

	@PATCH("{fId}/swapPositionWith/{sId}")
	suspend fun swapTablePositions(
		@Path("fId") fId: Long,
		@Path("sId") sId: Long,
	): Result<Unit>

	@DELETE("{id}")
	suspend fun deleteById(@Path("id") id: Long): Result<Unit>
}
