package io.dnajd.data.project_icon.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.project_icon.service.ProjectIconApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.io.File

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
		file: File,
	): Result<Unit> {
		val formData = MultipartBody.Part.createFormData(
			"file",
			file.name,
			file.asRequestBody("image/${file.extension}".toMediaTypeOrNull()),
		)

		return factory.updateByProjectId(
			projectId = projectId,
			file = formData,
		)
	}
}

interface ProjectIconApi {
	@GET("projectId/{projectId}")
	suspend fun getByProjectId(
		@Path("projectId") projectId: Long,
	): Result<ResponseBody>

	@Multipart
	@PUT("projectId/{projectId}")
	suspend fun updateByProjectId(
		@Path("projectId") projectId: Long,
		@Part file: MultipartBody.Part,
	): Result<Unit>
}
