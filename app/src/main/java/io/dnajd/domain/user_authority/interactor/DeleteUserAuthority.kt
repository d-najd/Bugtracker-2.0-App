package io.dnajd.domain.user_authority.interactor

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityRepository

class DeleteUserAuthority(
	private val repository: UserAuthorityRepository,
) {
	suspend fun await(userAuthority: UserAuthority): Boolean = repository.delete(userAuthority)
}
