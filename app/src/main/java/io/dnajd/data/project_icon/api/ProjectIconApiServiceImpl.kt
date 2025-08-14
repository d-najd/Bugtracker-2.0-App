package io.dnajd.data.project_icon.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project_icon.service.ProjectIconApiService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectIconApiServiceImpl : ProjectIconApiService {
	private val factory: ProjectIconApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.PROJECT_ICON)
		.build()
		.create(ProjectIconApi::class.java)

	override suspend fun getByProjectId(projectId: Long): Result<ResponseBody> =
		factory.getByProjectId(projectId)

	override suspend fun updateByProjectId(
		projectId: Long,
		file: MultipartBody.Part,
	): Result<Unit> {
		return Result.success(Unit)
	}
}

interface ProjectIconApi {
	@GET("projectId/{projectId}")
	suspend fun getByProjectId(
		@Path("projectId") projectId: Long,
	): Result<ResponseBody>

	@PUT("projectId/{projectId}")
	suspend fun updateByProjectId(
		@Path("projectId") projectId: Long,
	): Result<Unit>
}
