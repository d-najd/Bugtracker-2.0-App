package io.dnajd.data.project.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectRepository : RepositoryBase<Project, RepositoryBase.State<Project>>(State()) {
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

	override fun update(data: List<Project>) {
		val test = ""
		super.update(data)
	}
}