package io.dnajd.domain.project_table.service

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse

interface ProjectTableRepository {
	/**
	 * Gets all tables associated with [projectId]
	 * @param projectId id of the project in which the tables are located at
	 * @param includeTasks if true tasks wont be sent in the response
	 * @return list of received tables, empty list will be returned if the request failed
	 */
	suspend fun getAllByProjectId(
		projectId: Long,
		includeTasks: Boolean = true,
	): Result<ProjectTableListResponse>

	/**
	 * Gets one table
	 * @param id id of the table
	 * @param includeTasks if true tasks wont be sent in the response
	 * @return list of received tables, empty list will be returned if the request failed
	 */
	suspend fun getById(id: Long, includeTasks: Boolean = true): Result<ProjectTable>

	/**
	 * Creates project table
	 * @param table the pojo that is sent to the server
	 * @return received project from the server or null if the request failed
	 */
	suspend fun createTable(table: ProjectTable): Result<ProjectTable>

	/**
	 * Can update fields
	 * [ProjectTable.title]
	 */
	suspend fun updateTable(table: ProjectTable): Result<ProjectTable>

	/**
	 * Swaps the positions of 2 tables
	 * @param fId id of the first table
	 * @param sId id of the second table
	 * @return true if the request was successful false if it wasn't
	 */
	suspend fun swapTablePositions(fId: Long, sId: Long): Result<Unit>

	/**
	 * Deletes a table with given id
	 * @param id id of the table
	 * @return true if the request was successful false if it wasn't
	 */
	suspend fun deleteById(id: Long): Result<Unit>

}