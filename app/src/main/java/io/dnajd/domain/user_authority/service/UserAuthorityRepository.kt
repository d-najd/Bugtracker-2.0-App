package io.dnajd.domain.user_authority.service

import io.dnajd.domain.user_authority.model.UserAuthority

interface UserAuthorityRepository {

	/**
	 * Gets all userAuthorities associated for a given projectId
	 * @param projectId project id associated with the user authority
	 * @return list of projects matching the given [projectId], or empty list if the request failed
	 */
	suspend fun getAllByProjectId(projectId: Long): List<UserAuthority>

}