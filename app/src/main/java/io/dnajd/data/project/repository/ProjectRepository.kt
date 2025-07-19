package io.dnajd.data.project.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectRepository :
	RepositoryBase<List<Project>, RepositoryBase.State<List<Project>>>(State(emptyList())) {

	private val api: ProjectApiService = Injekt.get()

	@Composable
	fun dataCollectedById(id: Long): Project? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id
		) {
			stateCollected.data.firstOrNull { it.id == id }
		}
	}

	suspend fun fetchAllIfUninitialized(forceFetch: Boolean = false): Result<Unit> {
		if (!forceFetch && mutableState.value.fetchedData) {
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