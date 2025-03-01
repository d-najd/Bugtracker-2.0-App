package io.dnajd.bugtracker.ui.user_management

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIO
import io.dnajd.util.launchIONoQueue
import io.dnajd.util.launchUINoQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.function.BiFunction

typealias UserAuthorityMap = MutableMap<String, List<UserAuthority>>

class UserManagementScreenModel(
	val projectId: Long,

	private val userAuthorityRepository: UserAuthorityRepository = Injekt.get(),
) : StateScreenModel<UserManagementScreenState>(UserManagementScreenState.Loading) {
	private val _events: Channel<UserManagementEvent> = Channel(Int.MAX_VALUE)
	val events: Flow<UserManagementEvent> = _events.receiveAsFlow()

	private val mutex = Mutex()

	init {
		coroutineScope.launchIO {
			val userAuthoritiesResponse = userAuthorityRepository.getAllByProjectId(projectId)
				.onFailureWithStackTrace {
					_events.send(UserManagementEvent.FailedToRetrieveUserAuthorities)
					return@launchIO
				}

			val authorities = userAuthoritiesResponse.getOrThrow().data
			mutableState.update {
				UserManagementScreenState.Success(
					projectId = projectId,
					authorities = authorities,
				)
			}
		}
	}

	/**
	 * creates user authority if it does not exist or removes it if it does exist
	 */
	fun invertAuthority(userAuthority: UserAuthority) {
		mutex.launchIONoQueue(coroutineScope) {
			val successState = mutableState.value as UserManagementScreenState.Success

			if (successState.authorities.contains(userAuthority)) {
				deleteAuthorityInternal(userAuthority)
			} else {
				createAuthorityInternal(userAuthority)
			}
		}
	}

	fun createAuthority(userAuthority: UserAuthority) {
		mutex.launchIONoQueue(coroutineScope) {
			createAuthorityInternal(userAuthority)
		}
	}

	/**
	 * @throws IllegalArgumentException if the authority already exists
	 */
	@Throws(IllegalArgumentException::class)
	private suspend fun createAuthorityInternal(userAuthority: UserAuthority) {
		val successState = mutableState.value as UserManagementScreenState.Success
		val authorities = successState.authorities.toMutableList()

		if (authorities.contains(userAuthority)) {
			throw IllegalArgumentException("Authority already exists")
		}

		val createdAuthority =
			userAuthorityRepository.create(userAuthority).onFailureWithStackTrace {
				_events.send(UserManagementEvent.FailedToCreateUserAuthority)
				return
			}.getOrThrow()

		authorities.add(createdAuthority)
		mutableState.update {
			successState.copy(
				authorities = authorities
			)
		}
	}


	/**
	 * removes user authority from given project, if every authority is removed the user will be
	 * removed from the project as well, [agreed] must be true to prevent accidental removal of users
	 */
	fun deleteAuthority(userAuthority: UserAuthority, agreed: Boolean = false) {
		mutex.launchIONoQueue(coroutineScope) {
			deleteAuthorityInternal(userAuthority, agreed)
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
		val authorities = successState.authorities.toMutableList()

		if (!agreed && authorities.filter { it.username == userAuthority.username }.size <= 1) {
			mutableState.update {
				successState.copy(
					dialog = UserManagementDialog.ConfirmLastAuthorityRemoval(userAuthority)
				)
			}
			return
		}

		userAuthorityRepository.delete(userAuthority).onFailureWithStackTrace {
			_events.send(UserManagementEvent.UserAuthorityDoesNotExist)
			return
		}

		authorities.remove(userAuthority)
		mutableState.update {
			(mutableState.value as UserManagementScreenState.Success).copy(
				authorities = authorities,
			)
		}
	}

	fun showDialog(dialog: UserManagementDialog) {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as UserManagementScreenState.Success

			mutableState.update {
				successState.copy(dialog = dialog)
			}
		}
	}

	fun dismissDialog() {
		mutex.launchUINoQueue(coroutineScope) {
			val successState = mutableState.value as UserManagementScreenState.Success

			mutableState.update {
				successState.copy(dialog = null)
			}
		}
	}
}

sealed class UserManagementEvent {
	sealed class LocalizedMessage(@StringRes val stringRes: Int) : UserManagementEvent()
	object UserAuthorityDoesNotExist :
		LocalizedMessage(R.string.error_user_authority_does_not_exist)

	object FailedToRetrieveUserAuthorities :
		LocalizedMessage(R.string.error_failed_tor_retrieve_user_authorities)

	object FailedToCreateUserAuthority :
		LocalizedMessage(R.string.error_failed_to_create_user_authority)
}

sealed class UserManagementDialog {
	data class ConfirmLastAuthorityRemoval(val userAuthority: UserAuthority) :
		UserManagementDialog()

	object AddUserToProject : UserManagementDialog()
}

sealed class UserManagementScreenState {

	@Immutable
	object Loading : UserManagementScreenState()

	@Immutable
	data class Success(
		val projectId: Long,
		val authorities: List<UserAuthority>,
		val dialog: UserManagementDialog? = null,
	) : UserManagementScreenState() {
		fun getUsersWithAuthorities(): Map<String, List<UserAuthority>> {
			val authorityMap: UserAuthorityMap = mutableMapOf()
			for (authority in authorities) {
				authorityMap.compute(
					authority.username,
					BiFunction { _, u ->
						val mutableList = u?.toMutableList() ?: mutableListOf()
						mutableList.add(authority)
						return@BiFunction mutableList
					}
				)
			}
			return authorityMap.toSortedMap()
		}
	}

}