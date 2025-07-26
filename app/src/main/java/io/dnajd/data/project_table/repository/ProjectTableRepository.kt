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

	/**
	 * @param fetchTasks if true the tasks will be fetched and the repository for tasks updated
	 */
	suspend fun fetchByProjectIdIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = false,
	): Result<Set<ProjectTable>> {
		if (!forceFetch && lastFetchedByProjectIds().containsKey(projectId)) {
			return Result.success(dataKeysByProjectId(projectId))
		}

		val retrievedData = api
			.getAllByProjectId(
				projectId,
				fetchTasks,
			)
			.onFailure {
				return Result.failure(it)
			}
			.getOrThrow().data

		update(
			combineForUpdate(*retrievedData.toTypedArray()),
			projectId,
		)

		val newTasks = retrievedData.flatMap { table -> table.tasks!! }
		val combinedTasks = TableTaskRepository.combineForUpdate(*newTasks.toTypedArray())

		val tableIds = retrievedData
			.map { table -> table.id }
			.toLongArray()

		TableTaskRepository.update(
			combinedTasks,
			*tableIds,
		)

		return Result.success(retrievedData.toSet())
	}

	suspend fun fetchByIdIfStale(
		id: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = true,
	): Result<ProjectTable> {
		val table = dataKeyById(id)
		if (!forceFetch && table != null) {
			return Result.success(table)
		}

		val retrievedData = api
			.getById(
				id,
				fetchTasks,
			)
			.onFailure {
				return Result.failure(it)
			}
			.getOrThrow()

		update(
			combineForUpdate(retrievedData),
		)

		val newTasks = retrievedData.tasks!!
		val combinedTasks = TableTaskRepository.combineForUpdate(*newTasks.toTypedArray())

		TableTaskRepository.update(
			combinedTasks,
			retrievedData.id,
		)

		return Result.success(retrievedData)
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
				.associateWith { Date() },
		)

		mutableState.value = ProjectTableRepositoryState(
			data = dataWithoutTasks,
			lastFetchedByProjectIds = lastFetches,
		)
	}

	@Composable
	fun dataKeysCollectedByProjectId(projectId: Long): Set<ProjectTable> {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			projectId,
		) {
			stateCollected.data.keys
				.filter { it.projectId == projectId }
				.toSet()
		}
	}

	@Composable
	fun dataKeyCollectedById(id: Long): ProjectTable? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id,
		) {
			stateCollected.data.keys.firstOrNull { it.id == id }
		}
	}

	fun dataKeyById(id: Long): ProjectTable? {
		return state.value.data.keys.firstOrNull { it.id == id }
	}

	fun dataKeysByProjectId(projectId: Long): Set<ProjectTable> {
		return dataKeys()
			.filter { it.projectId == projectId }
			.toSet()
	}
}
