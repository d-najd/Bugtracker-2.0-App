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
	val lastFetchedByProjectIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<ProjectTable, Date>(data)

object ProjectTableRepository :
	RepositoryBase<ProjectTable, Date, ProjectTableRepositoryState>(ProjectTableRepositoryState()) {

	private val api: ProjectTableApiService = Injekt.get()

	private fun lastFetchedByProjectIds(): Map<Long, Date> = state.value.lastFetchedByProjectIds

	@Composable
	fun dataKeysCollectedByProjectId(projectId: Long): Set<ProjectTable> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			projectId
		) {
			stateCollected.data.keys
				.filter { it.projectId == projectId }
				.toSet()
		}
	}

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchByProjectIdIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && lastFetchedByProjectIds().containsKey(projectId)) {
			return Result.success(Unit)
		}
		return api
			.getAllByProjectId(
				projectId,
				fetchTasks
			)
			.onSuccess {
				val oldTables = data()
				val newTables = it.data
					.toSet()
					.associateWith { Date() }

				val tablesCombined = oldTables
					.filter { oldTableEntry ->
						newTables.any { newTable -> oldTableEntry.key.id != newTable.key.id }
					}
					.plus(newTables)

				update(
					tablesCombined,
					projectId
				)

				if (fetchTasks) {
					val oldTasks = TableTaskRepository.data()
					val newTasks = it.data
						.flatMap { table -> table.tasks!! }
						.toSet()
						.associateWith { Date() }

					val tasksCombined = oldTasks
						.filter { oldTask ->
							newTasks.any { newTask -> oldTask.key.id != newTask.key.id }
						}
						.plus(newTasks)

					val tableIds = newTables.keys
						.map { table -> table.id }
						.toLongArray()

					TableTaskRepository.update(
						tasksCombined,
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

		val lastFetches = lastFetchedByProjectIds().toMutableMap()
		lastFetches.putAll(
			lastFetchProjectsUpdated
				.toSet()
				.associateWith { Date() })

		mutableState.value = ProjectTableRepositoryState(
			data = dataWithoutTasks,
			lastFetchedByProjectIds = lastFetches
		)
	}
}