package io.dnajd.data.user_authority.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Date

data class UserAuthorityRepositoryState(
	override val data: Map<UserAuthority, Date> = emptyMap(),
	val lastFetchesByProjectId: Map<Long, Date> = emptyMap(),
) : RepositoryBase.State<UserAuthority, Date>(data)

object UserAuthorityRepository :
	RepositoryBase<UserAuthority, Date, UserAuthorityRepositoryState>(UserAuthorityRepositoryState()) {

	private val api: UserAuthorityApiService = Injekt.get()

	private fun lastFetchesByProjectId() = state.value.lastFetchesByProjectId

	private fun dataByProjectId(projectId: Long): Set<UserAuthority> = dataKeys()
		.filter { it.projectId == projectId }
		.toSet()

	suspend fun fetchByProjectIdIfStale(
		projectId: Long,
		forceFetch: Boolean = false,
	): Result<Set<UserAuthority>> {
		if (!forceFetch && lastFetchesByProjectId().containsKey(projectId)) {
			return Result.success(dataByProjectId(projectId))
		}

		api
			.getAllByProjectId(projectId)
			.onFailure { return Result.failure(it) }
			.getOrThrow().data
		TODO()
	}

	fun update(
		data: Map<UserAuthority, Date>,
		vararg lastFetchProjectsUpdated: Long,
	) {
		TODO()
	}
}
