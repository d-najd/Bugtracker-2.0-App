package io.dnajd.data.project.repository

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

object ProjectRepository :
	RepositoryBase<Project, Long, Date, ProjectRepositoryState>(ProjectRepositoryState()) {

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
			data = retrievedData.data.associateWith { defaultCacheValue() },
			updateLastFullFetch = true,
		)

		return Result.success(dataKeys())
	}

	suspend fun fetchOneIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
	): Result<Project> {
		val previousProject = dataKeysById(projectId).firstOrNull()
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

	override fun <T : Long> delete(vararg dataById: T) {
		super.delete(*dataById)

		val tableIds = ProjectTableRepository
			.dataKeysByProjectIds(*dataById.toLongArray())
			.map { it.id }

		ProjectTableRepository.delete(*tableIds.toTypedArray())

		val authorities = UserAuthorityRepository.dataKeysByProjectId(*dataById.toLongArray())
		UserAuthorityRepository.delete(*authorities.toTypedArray())
	}
}
