package io.dnajd.data.project.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.util.putIfNoneOrReplaceIf
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectRepositoryState(
	override val data: Map<Project, Date> = emptyMap(),
	val lastFullFetch: Date? = null,
) : RepositoryBase.State<Project, Date>(data)

object ProjectRepository :
	RepositoryBase<Project, Date, ProjectRepositoryState>(ProjectRepositoryState()) {

	private val api: ProjectApiService = Injekt.get()

	@Composable
	fun dataKeysCollectedById(id: Long): Project? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id
		) {
			stateCollected.data.keys.firstOrNull { it.id == id }
		}
	}

	suspend fun fetchAllIfStale(forceFetch: Boolean = false): Result<Unit> {
		if (!forceFetch && mutableState.value.lastFullFetch != null) {
			return Result.success(Unit)
		}
		return api
			.getAll()
			.onSuccess {
				update(
					data = it.data.associateWith { Date() },
					updateLastFullFetch = true,
				)
			}
			.map { }
	}

	suspend fun fetchOneIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
	): Result<Unit> {
		if (!forceFetch && dataKeys().any { it.id == projectId }) {
			return Result.success(Unit)
		}
		return api
			.getById(projectId)
			.onSuccess { retrievedProject ->
				val newData = data()
					.toMutableMap()
					.putIfNoneOrReplaceIf(
						retrievedProject,
						Date()
					) { k, _ ->
						k.id == projectId
					}

				update(
					data = newData,
					updateLastFullFetch = false,
				)
			}
			.map { }
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

		mutableState.value = ProjectRepositoryState(
			data = data,
			lastFullFetch = newLastFullFetchDate,
		)
	}
}