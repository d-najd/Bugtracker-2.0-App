package io.dnajd.data.project.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.project_table.repository.ProjectTableRepository
import io.dnajd.data.user_authority.repository.UserAuthorityRepository
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.utils.onFailureWithStackTrace
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectRepositoryState(
	override val data: Map<Project, Date> = emptyMap(),
	val lastFullFetch: Date? = null,
) : RepositoryBase.State<Project, Date>(data)

object ProjectRepository : RepositoryBase<Project, Date, ProjectRepositoryState>(ProjectRepositoryState()) {

	private val api: ProjectApiService = Injekt.get()

	suspend fun fetchAllIfStale(forceFetch: Boolean = false): Result<Set<Project>> {
		if (!forceFetch && mutableState.value.lastFullFetch != null) {
			return Result.success(dataKeys())
		}

		val retrievedData = api
			.getAll()
			.onFailureWithStackTrace {
				return Result.failure(it)
			}
			.getOrThrow()

		update(
			data = retrievedData.data.associateWith { Date() },
			updateLastFullFetch = true,
		)

		return Result.success(dataKeys())
	}

	suspend fun fetchOneIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
	): Result<Project> {
		val previousProject = dataKeyById(projectId)
		if (!forceFetch && previousProject != null) {
			return Result.success(previousProject)
		}

		val retrievedData = api
			.getById(projectId)
			.onFailure {
				return Result.failure(it)
			}
			.getOrThrow()

		update(
			combineForUpdate(retrievedData),
			updateLastFullFetch = false,
		)

		return Result.success(retrievedData)
	}

	fun update(
		data: Map<Project, Date>,
		updateLastFullFetch: Boolean = false,
	) {
		val newLastFullFetchDate = if (updateLastFullFetch) {
			Date()
		} else {
			mutableState.value.lastFullFetch
		}

		mutableState.value = state.value.copy(
			data = data,
			lastFullFetch = newLastFullFetchDate,
		)
	}

	override fun delete(vararg dataById: Any) {
		super.delete(dataById)

		@Suppress("UNCHECKED_CAST") val dataAsLongId = (dataById.toSet() as Set<Long>).toLongArray()

		val tables = ProjectTableRepository.dataKeysByProjectIds(*dataAsLongId)

		ProjectTableRepository.delete(
			*tables
				.map { it.id }
				.toTypedArray(),
		)

		val authorities = UserAuthorityRepository.dataKeysByProjectId(*dataAsLongId)
		UserAuthorityRepository.delete(*authorities.toTypedArray())
	}

	@Composable
	fun dataKeyCollectedById(id: Long): Project? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id,
		) {
			stateCollected.data.keys.firstOrNull { it.id == id }
		}
	}

	fun dataKeyById(id: Long): Project? {
		return state.value.data.keys.firstOrNull { it.id == id }
	}
}
