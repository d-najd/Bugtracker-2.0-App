package io.dnajd.data.project_table.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_table.model.ProjectTableBasic
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTaskBasic
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectTableRepositoryState(
	override val data: Map<ProjectTableBasic, Date?> = emptyMap(),
	val lastFetchByProjectId: Map<Long, Date?> = emptyMap(),
) : RepositoryBase.State<ProjectTableBasic>(data)

object ProjectTableRepository :
	RepositoryBase<ProjectTableBasic, ProjectTableRepositoryState>(ProjectTableRepositoryState()) {

	private val api: ProjectTableApiService = Injekt.get()

	private fun lastFetchByProjectId(): Map<Long, Date?> = state.value.lastFetchByProjectId

	@Composable
	fun dataCollectedByProjectId(projectId: Long): Set<ProjectTableBasic> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			projectId
		) {
			stateCollected.data.keys
				.filter { it.id == projectId }
				.toSet()
		}
	}

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchAllIfUninitialized(
		forceFetch: Boolean = false,
		projectId: Long,
		fetchTasks: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && lastFetchByProjectId().containsKey(projectId)) {
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

				update(
					tables,
					projectId
				)

				if (fetchTasks) {
					val tasks = it.data
						.flatMap { table -> table.tasks }
						.map { table -> TableTaskBasic(table) }
						.toSet()

					val tableIds = tables
						.map { table -> table.id }
						.toLongArray()
					TableTaskRepository.update(
						tasks,
						*tableIds
					)
				}
			}
			.map { }
	}

	/**
	 * @param lastFetchProjectsUpdated which projects should be notified that they have been updated
	 */
	fun update(
		data: Set<ProjectTableBasic>,
		vararg lastFetchProjectsUpdated: Long,
	) {		// TODO the value will all be updated like this, maybe edit it so that values get replaced/added?

		val lastFetches = lastFetchByProjectId().mapValues {
			if (lastFetchProjectsUpdated.contains(it.key)) Date() else it.value
		}

		mutableState.value = ProjectTableRepositoryState(
			data = data.associateWith { Date() },
			lastFetchByProjectId = lastFetches
		)
	}
}