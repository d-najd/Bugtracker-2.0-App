package io.dnajd.data.user_authority.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class UserAuthorityRepositoryState(
	override val data: Map<UserAuthority, Date> = emptyMap(),
	val lastFetchesByProjectIds: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<UserAuthority, Date>(data)

object UserAuthorityRepository :
	RepositoryBase<UserAuthority, UserAuthority, Date, UserAuthorityRepositoryState>(UserAuthorityRepositoryState()) {

	private val api: UserAuthorityApiService = Injekt.get()

	private fun lastFetchesByProjectIds() = state.value.lastFetchesByProjectIds

	suspend fun fetchByProjectIdIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
	): Result<Set<UserAuthority>> {
		if (!forceFetch && lastFetchesByProjectIds().containsKey(projectId)) {
			return Result.success(dataKeysByProjectId(projectId))
		}

		val retrievedData = api
			.getAllByProjectId(projectId)
			.onFailure { return Result.failure(it) }
			.getOrThrow().data

		update(
			combineForUpdate(
				newData = retrievedData.toTypedArray(),
				filterPredicate = filterByProjectIdPredicate(projectId),
			),
			projectId,
		)

		return Result.success(retrievedData.toSet())
	}

	/**
	 * Removes the old authorities matching projectId and replaces them with new
	 * @return the new authorities retrieved
	 */
	suspend fun reFetchReplaceByProjectId(
		projectId: Long,
	): Result<Set<UserAuthority>> {
		val retrievedData = api
			.getAllByProjectId(projectId)
			.onFailure { return Result.failure(it) }
			.getOrThrow().data

		val combinedData = combineForUpdate(
			newData = retrievedData.toTypedArray(),
			filterPredicate = filterByProjectIdPredicate(projectId),
		)

		update(
			combinedData,
			projectId,
		)

		return Result.success(retrievedData.toSet())
	}

	fun update(
		data: Map<UserAuthority, Date>,
		vararg lastFetchProjectsUpdated: Long,
	) {
		val lastFetches = lastFetchesByProjectIds().toMutableMap()
		lastFetches.putAll(
			lastFetchProjectsUpdated
				.toSet()
				.associateWith { Date() },
		)

		mutableState.value = state.value.copy(
			data = data,
			lastFetchesByProjectIds = lastFetches,
		)
	}

	override fun <T : UserAuthority> delete(vararg dataById: T) {
		val newData = state.value.data.filterKeys {
			!dataById.contains(it.getId())
		}

		val lastFetchesByProjectIds = lastFetchesByProjectIds().filterKeys { projectId ->
			newData.keys.any { it.projectId == projectId }
		}

		mutableState.value = state.value.copy(
			data = newData,
			lastFetchesByProjectIds = lastFetchesByProjectIds,
		)
	}

	private fun filterByProjectIdPredicate(projectId: Long): (Map.Entry<UserAuthority, Date>, Map.Entry<UserAuthority, Date>) -> Boolean {
		return { old, _ -> old.key.projectId != projectId }
	}

	fun dataKeysByProjectId(vararg projectIds: Long): Set<UserAuthority> = dataKeys()
		.filter { projectIds.contains(it.projectId) }
		.toSet()

	@Composable
	fun dataKeysCollectedByProjectId(projectId: Long): Set<UserAuthority> {
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
}
