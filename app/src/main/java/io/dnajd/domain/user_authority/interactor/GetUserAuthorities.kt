package io.dnajd.domain.user_authority.interactor

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityRepository

class GetUserAuthorities(
	private val repository: UserAuthorityRepository,
) {
	suspend fun await(projectId: Long): List<UserAuthority> =
		repository.getAllByProjectId(projectId)
}