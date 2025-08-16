package io.dnajd.data.project_icon.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.project_icon.model.ProjectIcon
import io.dnajd.domain.project_icon.service.ProjectIconApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class ProjectIconRepositoryState(
	override val data: Map<ProjectIcon, Date> = emptyMap(),
) : RepositoryBase.State<ProjectIcon, Date>(data)

object ProjectIconRepository :
	RepositoryBase<ProjectIcon, Long, Date, ProjectIconRepositoryState>(ProjectIconRepositoryState()) {
	private val api: ProjectIconApiService = Injekt.get()

	suspend fun fetchByProjectIdsIfStale(
		vararg dataById: Long,
		forceFetch: Boolean = false,
	): Result<Set<ProjectIcon>> {
		val previousDataKeyIds = state.value.data.keys.map { it.projectId }
		val dataNeedingUpdate = if (!forceFetch) dataById.subtract(previousDataKeyIds) else dataById.toSet()

		return coroutineScope {
			val tasks = dataNeedingUpdate.map { id ->
				async {
					val bitmap = api.getByProjectIdAsBitmap(id)
						.onFailure { error ->
							cancel(error.message + "", error)
						}
						.getOrThrow()

					ProjectIcon(
						projectId = id,
						bitmap = bitmap,
					)
				}
			}

			try {
				val result = tasks.awaitAll()
					.toSet()

				update(
					combineForUpdate(newData = result.toTypedArray()),
				)

				return@coroutineScope Result.success(result)
			} catch (e: Exception) {
				return@coroutineScope Result.failure<Set<ProjectIcon>>(e)
			}
		}
	}

	fun update(
		data: Map<ProjectIcon, Date>,
	) {
		mutableState.value = state.value.copy(
			data = data,
		)
	}
}
