package io.dnajd.domain.project_table.service

import io.dnajd.domain.project_table.model.ProjectTable

interface ProjectTableRepository {

    suspend fun getAll(projectId: Long): List<ProjectTable>

    suspend fun changeTitle(id: Long, newTitle: String): Boolean

    /**
     * swaps the positions of 2 tables
     * @param fId id of the first table
     * @param sId id of the second table
     */
    suspend fun swapPositionWith(fId: Long, sId: Long): Boolean

}