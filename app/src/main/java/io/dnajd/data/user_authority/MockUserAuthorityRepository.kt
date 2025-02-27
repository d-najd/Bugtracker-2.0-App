package io.dnajd.data.user_authority

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.domain.user_authority.service.UserAuthorityRepository

object MockUserAuthorityRepository : UserAuthorityRepository {
	override suspend fun getAllByProjectId(projectId: Long): Result<UserAuthorityListResponse> =
		Result.success(
			mockData()
		)

	override suspend fun create(userAuthority: UserAuthority): Result<UserAuthority> =
		Result.success(userAuthority)

	override suspend fun delete(userAuthority: UserAuthority): Result<Unit> = Result.success(Unit)

	private fun mockData() = UserAuthorityListResponse(
		listOf(
			UserAuthority("user1", 1, UserAuthorityType.project_owner),
			UserAuthority("user2", 1, UserAuthorityType.project_view),
			UserAuthority("user2", 1, UserAuthorityType.project_create),
			UserAuthority("user2", 1, UserAuthorityType.project_manage_users),
			UserAuthority("user3", 1, UserAuthorityType.project_view),
		)
	)
}
