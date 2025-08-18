package io.dnajd.domain.table_task.service

import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.model.TableTaskListResponse

interface TableTaskApiService {
	suspend fun getByTableId(
		tableId: Long,
		includeChildTasks: Boolean = false,
	): Result<TableTaskListResponse>

	/**
	 * Gets a single table task
	 * @param id id of the task
	 * @return received task from the server or null if the request failed
	 */
	suspend fun getById(id: Long): Result<TableTask>

	/**
	 * Creates table task
	 * @param task the pojo that is sent to the server
	 * @return received task from the server or null if the request failed
	 */
	suspend fun createTask(task: TableTask): Result<TableTask>

	/**
	 * Can update fields
	 * [TableTask.severity]
	 * [TableTask.title]
	 * [TableTask.description]
	 */
	suspend fun updateTask(task: TableTask): Result<TableTask>

	/**
	 * moves task from one position to another, this is different from moving tasks
	 * @param fId id of the first task
	 * @param sId id of the second task
	 * @return tasks that got changed due to the call
	 */
	suspend fun swapTaskPositions(
		fId: Long,
		sId: Long,
	): Result<TableTaskListResponse>

	/**
	 * moves task to the given position, this is different from swapping positions because every task
	 * that is between the starting and ending position have their position modified
	 * @param fId id of the first task
	 * @param sId id of the second task
	 * @return tasks that got changed due to the call
	 */
	suspend fun movePositionTo(
		fId: Long,
		sId: Long,
	): Result<TableTaskListResponse>

	/**
	 * Swaps the table of a task
	 * @param id id of the task
	 * @param tableId id of the table which the task will be moved to
	 * @return tasks that got changed due to the call
	 */
	suspend fun moveToTable(
		id: Long,
		tableId: Long,
	): Result<TableTaskListResponse>

	suspend fun delete(
		id: Long,
	): Result<TableTaskListResponse>
}
