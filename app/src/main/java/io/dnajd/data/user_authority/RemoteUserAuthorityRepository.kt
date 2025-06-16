package io.dnajd.data.user_authority

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteUserAuthorityRepository : UserAuthorityRepository {
	private val factory: UserAuthorityRepositoryApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl("")
		.build()
		.create(UserAuthorityRepositoryApi::class.java)

	override suspend fun getAllByProjectId(projectId: Long): Result<UserAuthorityListResponse> =
		factory.get(projectId)

	override suspend fun create(userAuthority: UserAuthority): Result<UserAuthority> =
		factory.create(userAuthority)

	override suspend fun delete(userAuthority: UserAuthority): Result<Unit> =
		factory.delete(userAuthority)
}

private interface UserAuthorityRepositoryApi {
	@GET("projectId/{projectId}")
	suspend fun get(
		@Path("projectId") projectId: Long,
	): Result<UserAuthorityListResponse>

	@POST("./")
	suspend fun create(
		@Body userAuthority: UserAuthority,
	): Result<UserAuthority>

	@DELETE("./")
	suspend fun delete(
		@Body userAuthority: UserAuthority,
	): Result<Unit>
}
