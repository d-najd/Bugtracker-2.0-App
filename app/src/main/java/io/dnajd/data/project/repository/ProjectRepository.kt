package io.dnajd.data.project.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectRepository :
	RepositoryBase<List<Project>, RepositoryBase.State<List<Project>>>(State(emptyList())) {

	private val api: ProjectApiService = Injekt.get()

	suspend fun fetchAllIfUninitialized(forceFetch: Boolean = true): Result<Unit> {
		if (forceFetch && mutableState.value.fetchedData) {
			return Result.success(Unit)
		}
		return api
			.getAll()
			.onSuccess {
				update(it.data)
			}
			.map { }
	}
}