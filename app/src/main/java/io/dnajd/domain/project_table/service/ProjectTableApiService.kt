package io.dnajd.domain.project_table.service

import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableListResponse

/**
 * NOTE another user may modify the [ProjectTable] after the user fetches the tasks and before he modifies them thus
 * causing inconsistency in the data on the client side, this can be solved by re-fetching all [ProjectTable] in
 * the current [Project]'s data
 */
interface ProjectTableApiService {
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
	suspend fun getById(
		id: Long,
		includeTasks: Boolean = true,
	): Result<ProjectTable>

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
	 * @return the tables that got modified due to the call
	 */
	suspend fun swapTablePositions(
		fId: Long,
		sId: Long,
	): Result<ProjectTableListResponse>

	/**
	 * Deletes a table with given id
	 * @param id id of the table
	 * @return the tables that got modified due to the call (deleted table is not included)
	 */
	suspend fun deleteById(id: Long): Result<ProjectTableListResponse>

}
