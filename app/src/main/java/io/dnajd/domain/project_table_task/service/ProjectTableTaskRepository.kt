package io.dnajd.domain.project_table_task.service

interface ProjectTableTaskRepository {
    /**
     * swaps the positions of 2 tasks
     * @param fId id of the first table
     * @param sId id of the second table
     */
    suspend fun swapTaskPositions(fId: Long, sId: Long): Boolean

}