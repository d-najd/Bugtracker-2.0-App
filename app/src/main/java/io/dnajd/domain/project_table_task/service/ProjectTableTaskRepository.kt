package io.dnajd.domain.project_table_task.service

interface ProjectTableTaskRepository {
    /**
     * moves task from one position to another, this is different from moving tasks
     * @param fId id of the first task
     * @param sId id of the second task
     */
    suspend fun swapPositionWith(fId: Long, sId: Long): Boolean

    /**
     * moves task to the given position, this is different from swapping positions because every task
     * that is between the starting and ending position have their position modified
     * @param fId id of the first task
     * @param sId id of the second task
     */
    suspend fun movePositionTo(fId: Long, sId: Long): Boolean

}