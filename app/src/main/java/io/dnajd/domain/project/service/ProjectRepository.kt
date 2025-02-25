package io.dnajd.domain.project.service

import io.dnajd.domain.project.model.Project

interface ProjectRepository {

	/**
	 * Gets all projects associated for a given username
	 * @param username username of the user which the projects are being requested for
	 * @return list of received projects, empty list will be returned if the request failed
	 */
	suspend fun getAll(username: String): List<Project>

	/**
	 * Gets single project by id
	 * @param id id of the table
	 * @return project associated with the id or null if it does not exist
	 */
	suspend fun get(id: Long): Project?

	/**
	 * Creates project
	 * @param project the pojo that is sent to the server
	 * @return received project from the server or null if the request failed
	 */
	suspend fun create(project: Project): Project?

	suspend fun updateNoBody(
		id: Long,
		title: String? = null,
		description: String? = null,
	): Boolean

	/**
	 * Deletes a project with given id
	 * @param id id of the table
	 * @return true if the request was successful false if it wasn't
	 */
	suspend fun delete(id: Long): Boolean
}