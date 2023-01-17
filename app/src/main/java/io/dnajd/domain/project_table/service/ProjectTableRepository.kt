package io.dnajd.domain.project_table.service

import io.dnajd.domain.project_table.model.ProjectTable

interface ProjectTableRepository {

    suspend fun getAll(projectId: Long): List<ProjectTable>

    /**
     * Creates project table
     * @param table the pojo that is sent to the server
     * @return received project from the server or null if the request failed
     */
    suspend fun create(table: ProjectTable): ProjectTable?

    /**
     * Changes the title on already existing table to a new title
     * @param id id of the table
     * @param newTitle the new title
     * @return true if the request was successfull false if it wasnt
     */
    suspend fun changeTitle(id: Long, newTitle: String): Boolean

    /**
     * swaps the positions of 2 tables
     * @param fId id of the first table
     * @param sId id of the second table
     * @return true if the request was successfull false if it wasnt
     */
    suspend fun swapPositionWith(fId: Long, sId: Long): Boolean

}