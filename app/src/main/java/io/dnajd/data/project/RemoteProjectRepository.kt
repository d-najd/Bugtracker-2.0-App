package io.dnajd.data.project

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.handleRetrofitRequest
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectListResponse
import io.dnajd.domain.project.service.ProjectRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectRepository : ProjectRepository {
	private val factory: ProjectRepositoryApi =
		Injekt.get<Retrofit.Builder>()
			.baseUrl(Urls.PROJECT)
			.build()
			.create(ProjectRepositoryApi::class.java)

	override suspend fun getAllByUsername(username: String): Result<ProjectListResponse> =
		handleRetrofitRequest { factory.getAllByUsername(username) }

	override suspend fun getById(id: Long): Result<Project> =
		handleRetrofitRequest { factory.getById(id) }

	override suspend fun createProject(project: Project): Result<Project> =
		handleRetrofitRequest { factory.createProject(project) }

	override suspend fun updateProject(
		project: Project,
	): Result<Project> = handleRetrofitRequest { factory.updateProject(project.id, project) }

	override suspend fun deleteById(id: Long): Result<Unit> =
		handleRetrofitRequest { factory.deleteById(id) }
}

interface ProjectRepositoryApi {
	@GET("user/{username}")
	fun getAllByUsername(@Path("username") username: String): Call<ProjectListResponse>

	@GET("{id}")
	fun getById(@Path("id") id: Long): Call<Project>

	@POST("./")
	fun createProject(@Body project: Project): Call<Project>

	@PUT("{id}")
	fun updateProject(
		@Path("id") id: Long,
		@Body project: Project,
	): Call<Project>

	@DELETE("{id}")
	fun deleteById(@Path("id") id: Long): Call<Unit>
}