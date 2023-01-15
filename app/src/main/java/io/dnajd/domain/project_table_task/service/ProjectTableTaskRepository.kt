package io.dnajd.domain.project_table_task.service

interface ProjectTableTaskRepository {
    /**
     * moves task from one position to another, this is different from swapping tasks
     * @param fId id of the first task
     * @param sId id of the second task
     */
    suspend fun moveTasks(fId: Long, sId: Long): Boolean

}