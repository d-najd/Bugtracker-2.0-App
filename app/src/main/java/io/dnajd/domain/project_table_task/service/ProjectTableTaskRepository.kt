package io.dnajd.domain.project_table_task.service

import io.dnajd.domain.project_table_task.model.ProjectTableTask

interface ProjectTableTaskRepository {

    /**
     * Gets a single table task
     * @param taskId id of the task
     * @return received task from the server or null if the request failed
     */
    suspend fun get(taskId: Long): ProjectTableTask?

    /**
     * Creates table task
     * @param task the pojo that is sent to the server
     * @return received task from the server or null if the request failed
     */
    suspend fun create(task: ProjectTableTask): ProjectTableTask?

    suspend fun updateNoBody(
        id: Long,
        title: String? = null,
        description: String? = null,
        severity: Int? = null
    ): Boolean

    /**
     * moves task from one position to another, this is different from moving tasks
     * @param fId id of the first task
     * @param sId id of the second task
     * @return true if the request was successful false if it wasn't
     */
    suspend fun swapPositionWith(fId: Long, sId: Long): Boolean

    /**
     * moves task to the given position, this is different from swapping positions because every task
     * that is between the starting and ending position have their position modified
     * @param fId id of the first task
     * @param sId id of the second task
     * @return true if the request was successful false if it wasn't
     */
    suspend fun movePositionTo(fId: Long, sId: Long): Boolean

    /**
     * Swaps the table of a task
     * @param id id of the task
     * @param tableId id of the table which the task will be moved to
     * @return true if the request was successful false if it wasn't
     */
    suspend fun swapTable(id: Long, tableId: Long): Boolean

}