package io.dnajd.data.project_table.repository

import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

data class ProjectTableRepositoryState(
	val fetchedTables: Boolean = false,
	val tables: List<ProjectTable> = emptyList(),
)

object ProjectTableRepository {
	private val _state: MutableStateFlow<ProjectTableRepositoryState> =
		MutableStateFlow(ProjectTableRepositoryState())
	val state: StateFlow<ProjectTableRepositoryState> = _state.asStateFlow()

	private val api: ProjectTableApiService = Injekt.get()

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchAllIfUninitialized(
		projectId: Long,
		forceFetch: Boolean = true,
		fetchTasks: Boolean = false,
	): Result<Unit> {
		if (forceFetch && _state.value.fetchedTables) {
			return Result.success(Unit)
		}
		return api
			.getAllByProjectId(
				projectId,
				fetchTasks
			)
			.onSuccess {                // TODO update Tasks repository if tasks fetched
				_state.value = _state.value.copy(
					fetchedTables = true,
					tables = it.data
				)
			}
			.map { }
	}

	fun update(tables: List<ProjectTable>) {

	}
}