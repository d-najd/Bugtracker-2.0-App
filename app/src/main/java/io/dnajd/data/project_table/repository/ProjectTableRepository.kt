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

		val result = api.getAllByProjectId(
			projectId,
			fetchTasks
		)
		val resultMapped = result.map { }
		if (result.isFailure) {
			return resultMapped
		}

		val newData = result.getOrThrow()

		val combinedData = combineForUpdate(
			Date(),
			*newData.data.toTypedArray()
		)
		update(
			combinedData,
			projectId
		)

		if (!fetchTasks) {
			return resultMapped
		}

		val newTasks = newData.data.flatMap { table -> table.tasks!! }
		val combinedTasks = TableTaskRepository.combineForUpdate(
			Date(),
			*newTasks.toTypedArray()
		)

		val tableIds = newData.data
			.map { table -> table.id }
			.toLongArray()

		TableTaskRepository.update(
			combinedTasks,
			*tableIds
		)

		return resultMapped
	}

	suspend fun fetchByIdIfStale(
		id: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = true,
	): Result<Unit> {
		if (!forceFetch && data().any { it.key.id == id }) {
			return Result.success(Unit)
		}

		val result = api.getById(
			id,
			fetchTasks
		)
		val resultMapped = result.map { }
		if (result.isFailure) {
			return resultMapped
		}

		val newData = result.getOrThrow()
		val combinedData = combineForUpdate(
			Date(),
			newData
		)
		update(combinedData)

		return resultMapped
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