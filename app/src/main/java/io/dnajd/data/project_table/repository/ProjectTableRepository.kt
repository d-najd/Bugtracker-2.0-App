package io.dnajd.data.project_table.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.table_task.repository.TableTaskRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.model.TableTask
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectTableRepositoryState(
	override val data: Map<ProjectTable, Date> = emptyMap(),
	val lastFetchedByProjectIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<ProjectTable, Date>(data)

object ProjectTableRepository :
	RepositoryBase<ProjectTable, Long, Date, ProjectTableRepositoryState>(ProjectTableRepositoryState()) {

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
			return Result.success(dataKeysByProjectIds(projectId))
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
			updateTasks = fetchTasks,
			projectId,
		)

		return Result.success(retrievedData.toSet())
	}

	suspend fun fetchByIdIfStale(
		id: Long,
		forceFetch: Boolean = false,
		fetchTasks: Boolean = true,
	): Result<ProjectTable> {
		val table = dataKeysById(id).firstOrNull()
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
			updateTasks = fetchTasks,
		)

		return Result.success(retrievedData)
	}

	/**
	 * @param updateTasks if true will first filter out all the [TableTask] from [TableTaskRepository]
	 * matching by [ProjectTable.id], and then add the new ones
	 * @param lastFetchProjectsUpdated which projects should be notified that they have been updated
	 */
	fun update(
		data: Map<ProjectTable, Date>,
		updateTasks: Boolean = false,
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

		if (!updateTasks) {
			return
		}

		val newTasks = data.keys
			.flatMap { table -> table.tasks ?: emptyList() }
			.associateWith { Date() }

		val combinedTasks = TableTaskRepository
			.data()
			.filterKeys { oldTask -> newTasks.none { newTask -> oldTask.tableId == newTask.key.tableId } }
			.plus(newTasks)

		val tableIds = data.keys
			.map { table -> table.id }
			.toLongArray()

		TableTaskRepository.update(
			combinedTasks,
			*tableIds,
		)
	}

	override fun <T : Long> delete(vararg dataById: T) {
		val newData = state.value.data.filterKeys {
			!dataById.contains(it.getId())
		}

		val newLastFetchByProjectId = lastFetchedByProjectIds().filterKeys { projectId ->
			newData.keys.any { it.projectId == projectId }
		}

		mutableState.value = state.value.copy(
			data = newData,
			lastFetchedByProjectIds = newLastFetchByProjectId,
		)

		val tasksIds = TableTaskRepository
			.dataByTableIds(*dataById.toLongArray())
			.map { it.key.id }

		TableTaskRepository.delete(*tasksIds.toTypedArray())
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

	fun dataKeysByProjectIds(vararg projectIds: Long): Set<ProjectTable> {
		return dataKeys()
			.filter { projectIds.contains(it.projectId) }
			.toSet()
	}
}
