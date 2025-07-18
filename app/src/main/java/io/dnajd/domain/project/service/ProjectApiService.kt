package io.dnajd.domain.project.service

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectListResponse

interface ProjectApiService {
	/**
	 * Gets all projects associated with current user
	 * @return list of received projects, empty list will be returned if the request failed
	 */
	suspend fun getAll(): Result<ProjectListResponse>

	/**
	 * Gets single project by id
	 * @param id id of the table
	 * @return project associated with the id or null if it does not exist
	 */
	suspend fun getById(id: Long): Result<Project>

	/**
	 * Creates project
	 * @param project the pojo that is sent to the server
	 * @return received project from the server or null if the request failed
	 */
	suspend fun createProject(project: Project): Result<Project>

	/**
	 * Can update fields:
	 * [Project.title]
	 * [Project.description]
	 */
	suspend fun updateProject(project: Project): Result<Project>

	/**
	 * Deletes a project with given id
	 * @param id id of the table
	 * @return true if the request was successful false if it wasn't
	 */
	suspend fun deleteById(id: Long): Result<Unit>
}