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

}