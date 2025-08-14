package io.dnajd.data.project.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectListResponse
import io.dnajd.domain.project.service.ProjectApiService
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectApiServiceImpl : ProjectApiService {
	private val factory: ProjectApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.PROJECT)
		.build()
		.create(ProjectApi::class.java)

	override suspend fun getAll(): Result<ProjectListResponse> = factory.getAll()

	override suspend fun getById(id: Long): Result<Project> = factory.getById(id)

	override suspend fun createProject(project: Project): Result<Project> = factory.createProject(project)

	override suspend fun updateProject(
		project: Project,
	): Result<Project> = factory.updateProject(
		project.id,
		project,
	)

	override suspend fun deleteById(id: Long): Result<Unit> = factory.deleteById(id)
}

interface ProjectApi {
	@GET("allByUsername")
	suspend fun getAll(
		// @Header("Authorization") authTest: String = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiaXNzIjoiZC1uYWpkLmJ1Z3RyYWNrZXIuYmFja2VuZCIsImF1ZCI6ImQtbmFqZC5idWd0cmFja2VyLmFuZHJvaWQiLCJzdWIiOiJkaW10aHJvdzEyMyIsImlhdCI6MTc0OTc2MTQ0NiwiZXhwIjoyMDY1MTIxNDQ2fQ.zcSuluRriiRxa6MMp6xIisulwKyI1S1pJajqaHFNQa1bxMBWlY3UzviYoXVyq13ZvXg4X9yO-0Lu-_bPWrYljA",
	): Result<ProjectListResponse>

	@GET("{id}")
	suspend fun getById(@Path("id") id: Long): Result<Project>

	@POST("./")
	suspend fun createProject(@Body project: Project): Result<Project>

	@PUT("{id}")
	suspend fun updateProject(
		@Path("id") id: Long,
		@Body project: Project,
	): Result<Project>

	@DELETE("{id}")
	suspend fun deleteById(@Path("id") id: Long): Result<Unit>
}
