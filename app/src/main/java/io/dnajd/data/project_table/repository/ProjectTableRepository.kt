package io.dnajd.data.project_table.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectTableRepositoryState(
	override val data: Map<ProjectTable, Date> = emptyMap(),
	val lastFetchByProjectId: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<ProjectTable, Date>(data)

object ProjectTableRepository :
	RepositoryBase<ProjectTable, Date, ProjectTableRepositoryState>(ProjectTableRepositoryState()) {

	private val api: ProjectTableApiService = Injekt.get()

	private fun lastFetchByProjectId(): Map<Long, Date> = state.value.lastFetchByProjectId

	@Composable
	fun dataKeysCollectedByProjectId(projectId: Long): Set<ProjectTable> {
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

	fun dataByProjectId(projectId: Long): Map<ProjectTable, Date> {
		return state.value.data.filter { it.key.id == projectId }
	}

	/*
	fun dataKeysByProjectId(projectId: Long): Set<ProjectTable> {
		return state.value.data.keys
			.filter { it.id == projectId }
			.toSet()
	}
	 */

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchByProjectIdIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
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
				val tablesAsEntries = tables
					.toSet()
					.associateWith { Date() }

				update(
					tablesAsEntries,
					projectId
				)

				if (fetchTasks) {
					val tasksAsEntries = it.data
						.flatMap { table -> table.tasks!! }
						.toSet()
						.associateWith { Date() }

					val tableIds = tables
						.map { table -> table.id }
						.toLongArray()

					TableTaskRepository.update(
						tasksAsEntries,
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
		data: Map<ProjectTable, Date>,
		vararg lastFetchProjectsUpdated: Long,
	) {
		val dataWithoutTasks = data.mapKeys {
			it.key.copy(tasks = null)
		}

		val lastFetches = lastFetchByProjectId().mapValues {
			if (lastFetchProjectsUpdated.contains(it.key)) Date() else it.value
		}

		mutableState.value = ProjectTableRepositoryState(
			data = dataWithoutTasks,
			lastFetchByProjectId = lastFetches
		)
	}
}