package io.dnajd.data.project_table.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_table.model.ProjectTableBasic
import io.dnajd.domain.project_table.service.ProjectTableApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectTableRepository :
	RepositoryBase<Set<ProjectTableBasic>, RepositoryBase.State<Set<ProjectTableBasic>>>(State(emptySet())) {

	private val api: ProjectTableApiService = Injekt.get()

	@Composable
	fun dataCollectedByProjectId(projectId: Long): Set<ProjectTableBasic> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			projectId
		) {
			stateCollected.data
				.filter { it.id == projectId }
				.toSet()
		}
	}

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchAllIfUninitialized(
		projectId: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && state.value.fetchedData) {
			return Result.success(Unit)
		}
		return api
			.getAllByProjectId(
				projectId,
				fetchTasks
			)
			.onSuccess {
				val tables = it.data
					.map { table -> ProjectTableBasic(table) }
					.toSet()

				update(tables)

				if (fetchTasks) {
					val tasks = it.data
						.flatMap { table -> table.tasks }
						.toSet()
					TableTaskRepository.update(tasks)
				}
			}
			.map { }
	}
}