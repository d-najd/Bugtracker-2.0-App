package io.dnajd.domain.user_authority.service

import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityListResponse
import io.dnajd.domain.user_authority.model.UserAuthorityType

interface UserAuthorityApiService {

	/**
	 * Gets all userAuthorities associated for a given projectId
	 * @param projectId project id associated with the user authority
	 * @return list of projects matching the given [projectId], or empty list if the request failed
	 */
	suspend fun getAllByProjectId(projectId: Long): Result<UserAuthorityListResponse>

	/**
	 * Adds ar removes authority
	 * @param userAuthority the pojo that will be sent to the server
	 * @param value if true will add the authority if false will remove it, nothing will happen
	 * the authority is already in that state
	 * @throws IllegalArgumentException if the authority is [UserAuthorityType.project_owner]
	 */
	@Throws(IllegalArgumentException::class)
	suspend fun modifyAuthority(
		userAuthority: UserAuthority,
		value: Boolean,
	): Result<Unit>
}