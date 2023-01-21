package io.dnajd.domain.project.service

import io.dnajd.domain.project.model.Project

interface ProjectRepository {

    /**
     * Gets all projects associated for a given username
     * @param username username of the user which the projects are being requested for
     * @return list of received projects, empty list will be returned if the request failed
     */
    suspend fun getAll(username: String): List<Project>

    /**
     * Creates project
     * @param project the pojo that is sent to the server
     * @return received project from the server or null if the request failed
     */
    suspend fun create(project: Project): Project?

    /**
     * Changes the title on already existing project to a new title
     * @param id id of the table
     * @param newTitle the new title
     * @return true if the request was successful false if it wasn't
     */
    suspend fun changeTitle(id: Long, newTitle: String): Boolean

    /**
     * Deletes a project with given id
     * @param id id of the table
     * @return true if the request was successful false if it wasn't
     */
    suspend fun delete(id: Long): Boolean
}