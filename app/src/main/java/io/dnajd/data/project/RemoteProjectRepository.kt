package io.dnajd.data.project

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectListResponse
import io.dnajd.domain.project.service.ProjectRepository
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectRepository : ProjectRepository {
	private val factory: ProjectRepositoryApi =
		Injekt.get<Retrofit.Builder>()
			.baseUrl("${Urls.PROJECT.getAppendedUrl()}/").build()
			.create(ProjectRepositoryApi::class.java)

	override suspend fun getAll(username: String): Result<ProjectListResponse> =
		factory.getProjectsByUsername(username)

	override suspend fun get(id: Long): Result<Project> =
		factory.getProjectById(id)

	override suspend fun create(project: Project): Result<Project> =
		factory.createProject(project)

	override suspend fun updateNoBody(
		id: Long,
		title: String?,
		description: String?
	): Result<Unit> = factory.updateNoBody(
		id = id,
		title = title,
		description = description
	)

	override suspend fun delete(id: Long): Result<Unit> =
		factory.deleteProject(id)
}

interface ProjectRepositoryApi {
	@GET("user/{username}")
	fun getProjectsByUsername(@Path("username") username: String): Result<ProjectListResponse>

	@GET("{id}")
	fun getProjectById(@Path("id") id: Long): Result<Project>

	@POST(Urls.PROJECT.appendedUrlLocal)
	fun createProject(@Body project: Project): Result<Project>

	/**
	 * Do not modify [returnBody]
	 */
	@PUT("{id}")
	fun updateNoBody(
		@Path("id") id: Long,
		@Query("title") title: String? = null,
		@Query("description") description: String? = null,
		@Query("returnBody") returnBody: Boolean = false,
	): Result<Unit>

	@DELETE("{id}")
	fun deleteProject(@Path("id") id: Long): Result<Unit>
}