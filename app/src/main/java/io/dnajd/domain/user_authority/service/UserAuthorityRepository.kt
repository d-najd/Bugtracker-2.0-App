package io.dnajd.domain.user_authority.service

import io.dnajd.domain.user_authority.model.UserAuthority

interface UserAuthorityRepository {

	/**
	 * Gets all userAuthorities associated for a given projectId
	 * @param projectId project id associated with the user authority
	 * @return list of projects matching the given [projectId], or empty list if the request failed
	 */
	suspend fun getAllByProjectId(projectId: Long): List<UserAuthority>

	/**
	 * Creates user authority
	 * @param userAuthority the pojo that is sent to the server
	 * @return received userAuthority from the server or null if the request failed
	 */
	suspend fun create(userAuthority: UserAuthority): UserAuthority?

	/**
	 * Deletes given userAuthority
	 * @param userAuthority pojo that is to be removed
	 * @return true if the request was successful false if it wasn't
	 */
	suspend fun delete(userAuthority: UserAuthority): Boolean

}