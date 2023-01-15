package io.dnajd.domain.project.service

import io.dnajd.domain.project.model.Project

interface ProjectRepository {

    suspend fun getAll(username: String): List<Project>

}