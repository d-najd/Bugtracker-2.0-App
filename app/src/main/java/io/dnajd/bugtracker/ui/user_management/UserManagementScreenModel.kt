package io.dnajd.bugtracker.ui.user_management

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.data.user_authority.repository.UserAuthorityRepository
import io.dnajd.data.user_authority.repository.UserAuthorityRepository.state
import io.dnajd.domain.base.onFailureWithStackTrace
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.function.BiFunction

typealias UserAuthorityMap = MutableMap<String, List<UserAuthority>>

class UserManagementScreenModel(
	val projectId: Long,

	private val userAuthorityApiService: UserAuthorityApiService = Injekt.get(),
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get(),
) : StateScreenModel<UserManagementScreenState>(UserManagementScreenState.Loading(projectId)) {
	private val _events: MutableSharedFlow<UserManagementEvent> = MutableSharedFlow()
	val events: SharedFlow<UserManagementEvent> = _events.asSharedFlow()

	private val mutex = Mutex()

	init {
		mutex.launchIONoQueue(coroutineScope) {
			UserAuthorityRepository
				.fetchByProjectIdIfStale(projectId)
				.onFailureWithStackTrace {
					_events.emit(UserManagementEvent.FailedToRetrieveUserAuthorities)
					return@launchIONoQueue
				}
				.getOrThrow()

			val selfUsername = jwtAuthPreferenceStore
				.retrieveAccessToken()
				.getOrThrow()!!.subject!!

			mutableState.update {
				UserManagementScreenState.Success(
					projectId = projectId,
					selfUsername = selfUsername,
				)
			}
		}
	}

	/**
	 * Creates or removes user authority
	 * @param userAuthority the user authority
	 * @param value if true will create authority, if false will remove, if unset will flip the
	 * authority
	 * @throws IllegalArgumentException if supplied authority type is [UserAuthorityType.project_owner]
	 */
	fun modifyAuthority(
		userAuthority: UserAuthority,
		value: Boolean? = null,
	) = mutex.launchIONoQueue(coroutineScope) {
		if (value == null) {
			val successState = mutableState.value as UserManagementScreenState.Success

			if (successState
					.authoritiesCurrent()
					.contains(userAuthority)
			) {
				deleteAuthorityInternal(userAuthority)
			} else {
				createAuthorityInternal(userAuthority)
			}

			return@launchIONoQueue
		} else if (value) {
			createAuthorityInternal(userAuthority)
			return@launchIONoQueue
		} else {
			deleteAuthorityInternal(userAuthority)
			return@launchIONoQueue
		}
	}

	private suspend fun createAuthorityInternal(userAuthority: UserAuthority) {
		val successState = mutableState.value as UserManagementScreenState.Success

		if (successState
				.authoritiesCurrent()
				.contains(userAuthority)
		) {
			_events.emit(UserManagementEvent.AuthorityAlreadyExists)
			return
		}

		userAuthorityApiService
			.modifyAuthority(
				userAuthority,
				true,
			)
			.onFailureWithStackTrace {
				_events.emit(UserManagementEvent.FailedToCreateUserAuthority)
				return
			}
			.getOrThrow()

		UserAuthorityRepository
			.reFetchReplaceByProjectId(projectId)
			.onFailureWithStackTrace {
				_events.emit(UserManagementEvent.FailedToCreateUserAuthority)
				return
			}
	}


	/**
	 * removes user authority from given project, if every authority is removed the user will be
	 * removed from the project as well, [agreed] must be true to prevent accidental removal of users
	 */
	fun deleteAuthority(
		userAuthority: UserAuthority,
		agreed: Boolean = false,
	) {
		mutex.launchIONoQueue(coroutineScope) {
			deleteAuthorityInternal(
				userAuthority,
				agreed,
			)
		}
	}

	/**
	 * removes user authority from given project, if every authority is removed the user will be
	 * removed from the project as well, [agreed] must be true to prevent accidental removal of users
	 */
	private suspend fun deleteAuthorityInternal(
		userAuthority: UserAuthority,
		agreed: Boolean = false,
	) {
		val successState = mutableState.value as UserManagementScreenState.Success
		val currentUserAuthorities = successState
			.authoritiesCurrent()
			.filter { it.username == userAuthority.username }

		if (!agreed && (currentUserAuthorities.size <= 1 || userAuthority.authority == UserAuthorityType.project_view)) {
			mutableState.update {
				successState.copy(
					dialog = UserManagementDialog.ConfirmLastAuthorityRemoval(userAuthority),
				)
			}
			return
		}

		userAuthorityApiService
			.modifyAuthority(
				userAuthority,
				false,
			)
			.onFailureWithStackTrace {
				_events.emit(UserManagementEvent.FailedToModifyUserAuthority)
				return
			}

		UserAuthorityRepository
			.reFetchReplaceByProjectId(
				projectId,
			)
			.onFailureWithStackTrace {
				_events.emit(UserManagementEvent.FailedToCreateUserAuthority)
				return
			}
	}

	fun showDialog(dialog: UserManagementDialog) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as UserManagementScreenState.Success

			mutableState.update { successState.copy(dialog = dialog) }
		}
	}

	fun dismissDialog() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as UserManagementScreenState.Success

			mutableState.update { successState.copy(dialog = null) }
		}
	}
}

sealed class UserManagementEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : UserManagementEvent()

	data object FailedToModifyUserAuthority : LocalizedMessage(R.string.error_failed_to_modify_user_authority)

	data object FailedToRetrieveUserAuthorities :
		LocalizedMessage(R.string.error_failed_tor_retrieve_user_authorities)

	data object FailedToCreateUserAuthority : LocalizedMessage(R.string.error_failed_to_create_user_authority)

	data object AuthorityAlreadyExists : LocalizedMessage(R.string.error_user_authority_already_exists)
}

sealed class UserManagementDialog {
	data class ConfirmLastAuthorityRemoval(val userAuthority: UserAuthority) : UserManagementDialog()

	data object AddUserToProject : UserManagementDialog()
}

sealed class UserManagementScreenState(
	open val projectId: Long,
) {
	@Immutable
	data class Loading(override val projectId: Long) : UserManagementScreenState(projectId)

	@Immutable
	data class Success(
		override val projectId: Long,
		val selfUsername: String,
		val dialog: UserManagementDialog? = null,
	) : UserManagementScreenState(projectId) {
		fun authoritiesCurrent() = UserAuthorityRepository.dataKeysByProjectId(projectId)

		@Composable
		fun authoritiesCollectedByUser(): Map<String, List<UserAuthority>> {
			val stateCollected by state.collectAsState()

			return remember(
				stateCollected,
				projectId,
			) {
				val authorityMap: UserAuthorityMap = mutableMapOf()

				for (authority in stateCollected.data.keys) {
					authorityMap.compute(
						authority.username,
						BiFunction { _, u ->
							val mutableList = u?.toMutableList() ?: mutableListOf()
							mutableList.add(authority)
							return@BiFunction mutableList
						},
					)
				}

				authorityMap.toSortedMap()
			}
		}
	}
}
