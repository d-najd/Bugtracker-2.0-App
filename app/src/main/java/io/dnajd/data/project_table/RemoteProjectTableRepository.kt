package io.dnajd.data.project_table

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.handleRetrofitRequest
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse
import io.dnajd.domain.project_table.service.ProjectTableRepository
import retrofit2.Call
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
	private val factory: ProjectTableRepositoryApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.PROJECT_TABLE)
		.build()
		.create(ProjectTableRepositoryApi::class.java)

	override suspend fun getAllByProjectId(
		projectId: Long,
		includeTasks: Boolean,
	): Result<ProjectTableListResponse> = handleRetrofitRequest {
		factory.getAllByProjectId(
			projectId,
			includeTasks
		)
	}

	override suspend fun getById(
		id: Long,
		includeTasks: Boolean,
	): Result<ProjectTable> = handleRetrofitRequest {
		factory.getById(
			id,
			includeTasks
		)
	}

	override suspend fun createTable(table: ProjectTable): Result<ProjectTable> =
		handleRetrofitRequest {
			factory.createTable(
				table.projectId,
				table
			)
		}

	override suspend fun updateTable(
		table: ProjectTable,
	): Result<ProjectTable> = handleRetrofitRequest {
		factory.updateTable(
			table.id,
			table
		)
	}

	override suspend fun swapTablePositions(
		fId: Long,
		sId: Long,
	): Result<Unit> = handleRetrofitRequest {
		factory.swapTablePositions(
			fId,
			sId
		)
	}

	override suspend fun deleteById(id: Long): Result<Unit> =
		handleRetrofitRequest { factory.deleteById(id) }
}

private interface ProjectTableRepositoryApi {
	@GET("projectId/{projectId}")
	fun getAllByProjectId(
		@Path("projectId") projectId: Long,
		@Query("includeIssues") includeTasks: Boolean,
	): Call<ProjectTableListResponse>

	@GET("id/{id}")
	fun getById(
		@Path("id") id: Long,
		@Query("includeIssues") includeTasks: Boolean,
	): Call<ProjectTable>

	@POST("projectId/{projectId}")
	fun createTable(
		@Path("projectId") projectId: Long,
		@Body table: ProjectTable,
	): Call<ProjectTable>

	@PUT("{id}")
	fun updateTable(
		@Path("id") id: Long,
		@Body table: ProjectTable,
	): Call<ProjectTable>

	@PATCH("{fId}/swapPositionWith/{sId}")
	fun swapTablePositions(
		@Path("fId") fId: Long,
		@Path("sId") sId: Long,
	): Call<Unit>

	@DELETE("{id}")
	fun deleteById(@Path("id") id: Long): Call<Unit>
}