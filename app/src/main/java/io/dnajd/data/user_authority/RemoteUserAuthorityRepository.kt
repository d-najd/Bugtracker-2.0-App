package io.dnajd.data.user_authority

import io.dnajd.data.utils.handleRetrofitRequest
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteUserAuthorityRepository : UserAuthorityRepository {
	private val factory: UserAuthorityRepositoryApi =
		Injekt.get<Retrofit.Builder>()
			.baseUrl("").build()
			// .baseUrl(/* baseUrl = */ "${Urls.USER_AUTHORITY.getAppendedUrl()}/").build()
			.create(UserAuthorityRepositoryApi::class.java)

	override suspend fun getAllByProjectId(projectId: Long): Result<UserAuthorityListResponse> =
		handleRetrofitRequest { factory.get(projectId) }

	override suspend fun create(userAuthority: UserAuthority): Result<UserAuthority> =
		handleRetrofitRequest { factory.create(userAuthority) }

	override suspend fun delete(userAuthority: UserAuthority): Result<Unit> =
		handleRetrofitRequest { factory.delete(userAuthority) }
}

private interface UserAuthorityRepositoryApi {
	@GET("projectId/{projectId}")
	fun get(
		@Path("projectId") projectId: Long,
	): Call<UserAuthorityListResponse>

	// @POST(Urls.USER_AUTHORITY.appendedUrlLocal)
	@POST("./")
	fun create(
		@Body userAuthority: UserAuthority,
	): Call<UserAuthority>

	/*
	@HTTP(
		method = "DELETE",
		path = Urls.USER_AUTHORITY.appendedUrlLocal,
		hasBody = true,
	)
	 */
	@DELETE("./")
	fun delete(
		@Body userAuthority: UserAuthority,
	): Call<Unit>
}
