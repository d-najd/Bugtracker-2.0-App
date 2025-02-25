package io.dnajd.domain.user_authority.interactor

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityRepository

class CreateUserAuthority(
	private val repository: UserAuthorityRepository,
) {
	suspend fun awaitOne(userAuthority: UserAuthority): UserAuthority? =
		repository.create(userAuthority)
}
