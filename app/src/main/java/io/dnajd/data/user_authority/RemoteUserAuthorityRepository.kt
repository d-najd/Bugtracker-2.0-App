package io.dnajd.data.user_authority

import io.dnajd.data.utils.Urls
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteUserAuthorityRepository : UserAuthorityRepository {
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
	): Result<Unit> = when (userAuthority.authority) {
		UserAuthorityType.project_view -> {
			factory.modifyViewUserAuthority(
				userAuthority.projectId,
				value
			)
		}

		UserAuthorityType.project_create -> {
			factory.modifyCreateUserAuthority(
				userAuthority.projectId,
				value
			)
		}

		UserAuthorityType.project_edit -> {
			factory.modifyEditUserAuthority(
				userAuthority.projectId,
				value
			)
		}

		UserAuthorityType.project_delete -> {
			factory.modifyDeleteUserAuthority(
				userAuthority.projectId,
				value
			)
		}

		UserAuthorityType.project_manage_users -> {
			factory.modifyManageUserAuthority(
				userAuthority.projectId,
				value
			)
		}

		UserAuthorityType.project_owner -> {
			throw IllegalArgumentException("Can't modify project owner permission")
		}
	}
}

private interface UserAuthorityRepositoryApi {
	@GET("projectId/{projectId}")
	suspend fun get(
		@Path("projectId") projectId: Long,
	): Result<UserAuthorityListResponse>

	@PUT("projectId/{projectId}/view/{value}")
	suspend fun modifyViewUserAuthority(
		@Path("projectId") projectId: Long,
		@Path("value") value: Boolean,
	): Result<Unit>

	@PUT("projectId/{projectId}/create/{value}")
	fun modifyCreateUserAuthority(
		@Path("projectId") projectId: Long,
		@Path("value") value: Boolean,
	): Result<Unit>

	@PUT("projectId/{projectId}/edit/{value}")
	fun modifyEditUserAuthority(
		@Path("projectId") projectId: Long,
		@Path("value") value: Boolean,
	): Result<Unit>

	@PUT("projectId/{projectId}/delete/{value}")
	fun modifyDeleteUserAuthority(
		@Path("projectId") projectId: Long,
		@Path("value") value: Boolean,
	): Result<Unit>

	@PUT("projectId/{projectId}/manage/{value}")
	fun modifyManageUserAuthority(
		@Path("projectId") projectId: Long,
		@Path("value") value: Boolean,
	): Result<Unit>
}
