package io.dnajd.data.project.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

data class ProjectRepositoryState(
	val fetchedProjects: Boolean = false,
	val projects: List<Project> = emptyList(),
)

object ProjectRepository {
	private val _state: MutableStateFlow<ProjectRepositoryState> =
		MutableStateFlow(ProjectRepositoryState())
	val state: StateFlow<ProjectRepositoryState> = _state.asStateFlow()

	private val api: ProjectApiService = Injekt.get()

	@Composable
	fun projectsCollected(): List<Project> {
		val stateCollected by state.collectAsState()
		return remember(stateCollected) {
			stateCollected.projects
		}
	}

	fun projects(): List<Project> = state.value.projects

	suspend fun fetchAllIfUninitialized(forceFetch: Boolean = true): Result<Unit> {
		if (forceFetch && _state.value.fetchedProjects) {
			return Result.success(Unit)
		}
		return api
			.getAll()
			.onSuccess {
				_state.value = _state.value.copy(
					fetchedProjects = true,
					projects = it.data
				)
			}
			.map { }
	}

	fun update(projects: List<Project>) {
		_state.value = _state.value.copy(
			projects = projects,
			fetchedProjects = true,
		)
	}

	// Should only have methods for fetching one or multiple and modifying one or multiple
}