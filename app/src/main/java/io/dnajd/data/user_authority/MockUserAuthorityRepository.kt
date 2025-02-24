package io.dnajd.data.user_authority

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.domain.user_authority.service.UserAuthorityRepository

object MockUserAuthorityRepository : UserAuthorityRepository {
	override suspend fun getAllByProjectId(projectId: Long): List<UserAuthority> = listOf(
		UserAuthority("user1", 1, UserAuthorityType.project_owner),
		UserAuthority("user2", 1, UserAuthorityType.project_view),
		UserAuthority("user2", 1, UserAuthorityType.project_create),
		UserAuthority("user2", 1, UserAuthorityType.project_manage_users),
		UserAuthority("user3", 1, UserAuthorityType.project_view),
	)

	@Suppress("RedundantNullableReturnType")
	override suspend fun create(userAuthority: UserAuthority): UserAuthority? = userAuthority

	override suspend fun delete(userAuthority: UserAuthority): Boolean = true

}
