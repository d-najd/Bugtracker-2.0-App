package io.dnajd.data.user_authority.api

import io.dnajd.data.utils.Urls
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object UserAuthorityApiServiceImpl : UserAuthorityApiService {
	private val factory: UserAuthorityRepositoryApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.PROJECT_AUTHORITY)
		.build()
		.create(UserAuthorityRepositoryApi::class.java)

	override suspend fun getAllByProjectId(projectId: Long): Result<UserAuthorityListResponse> =
		factory.get(projectId)

	override suspend fun modifyAuthority(
		userAuthority: UserAuthority,
		value: Boolean,
	): Result<Unit> {
		if (userAuthority.authority == UserAuthorityType.project_owner) {
			throw IllegalArgumentException("Can't modify project owner permission")
		}
		if (userAuthority.authority == UserAuthorityType.project_manage_users) {
			return factory.modifyManagerAuthority(
				userAuthority,
				value,
			)
		}
		return factory.modifyUserAuthority(
			userAuthority,
			value,
		)
	}
}

private interface UserAuthorityRepositoryApi {
	@GET("projectId/{projectId}")
	suspend fun get(
		@Path("projectId") projectId: Long,
	): Result<UserAuthorityListResponse>

	@POST("userAuthority/value/{value}")
	suspend fun modifyUserAuthority(
		@Body projectAuthorityId: UserAuthority,
		@Path("value") value: Boolean,
	): Result<Unit>

	@POST("managerAuthority/value/{value}")
	suspend fun modifyManagerAuthority(
		@Body projectAuthorityId: UserAuthority,
		@Path("value") value: Boolean,
	): Result<Unit>
}
