package io.dnajd.data.project.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.service.ProjectApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectRepositoryState(
	override val data: Map<Project, Date?> = emptyMap(),
	val lastFullFetch: Date? = null,
) : RepositoryBase.State<Project>(data)

object ProjectRepository :
	RepositoryBase<Project, ProjectRepositoryState>(ProjectRepositoryState()) {

	private val api: ProjectApiService = Injekt.get()

	@Composable
	fun dataCollectedById(id: Long): Project? {
		val stateCollected by state.collectAsState()
		return remember(
			stateCollected,
			id
		) {
			stateCollected.data.keys.firstOrNull { it.id == id }
		}
	}

	suspend fun fetchAllIfUninitialized(forceFetch: Boolean = false): Result<Unit> {
		if (!forceFetch && mutableState.value.lastFullFetch != null) {
			return Result.success(Unit)
		}
		return api
			.getAll()
			.onSuccess {
				update(it.data.toSet())
			}
			.map { }
	}

	fun update(
		data: Set<Project>,
		updateLastFullFetch: Boolean = true,
	) {
		mutableState.value = ProjectRepositoryState(
			data = data.associateWith { Date() },
			lastFullFetch = Date(),
		)
	}

	// suspend fun fetchOneIfUninitialized()
}