package io.dnajd.bugtracker.ui.project_user_management

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.ui.project.ProjectDialog
import io.dnajd.bugtracker.ui.project.ProjectScreenState
import io.dnajd.domain.user_authority.interactor.CreateUserAuthority
import io.dnajd.domain.user_authority.interactor.DeleteUserAuthority
import io.dnajd.domain.user_authority.interactor.GetUserAuthorities
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.function.BiFunction

typealias UserAuthorityMap = MutableMap<String, List<UserAuthority>>

class ProjectUserManagementScreenModel(
    context: Context,
    val projectId: Long,

    private val getUserAuthorities: GetUserAuthorities = Injekt.get(),
    private val createUserAuthority: CreateUserAuthority = Injekt.get(),
    private val deleteUserAuthority: DeleteUserAuthority = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectUserManagementScreenState>(context,
    ProjectUserManagementScreenState.Loading
) {
    init {
        coroutineScope.launchIO {
            val authorities = getUserAuthorities.await(projectId)
            if(authorities.isNotEmpty()) {
                mutableState.update {
                    ProjectUserManagementScreenState.Success(
                        projectId = projectId,
                        authorities = authorities,
                    )
                }
            }
        }
    }

    /**
     * creates user authority if it does not exist or removes it if it does exist
     */
    fun invertAuthority(userAuthority: UserAuthority) {
        if((mutableState.value as ProjectUserManagementScreenState.Success).authorities.contains(userAuthority)) {
            deleteAuthority(userAuthority)
        } else {
            createAuthority(userAuthority)
        }
    }

    private fun createAuthority(userAuthority: UserAuthority) {
        coroutineScope.launchIO {
            createUserAuthority.awaitOne(userAuthority)?.let { persistedUserAuthority ->
                val authorities = (mutableState.value as ProjectUserManagementScreenState.Success).authorities.toMutableList()
                authorities.add(persistedUserAuthority)
                mutableState.update {
                    (mutableState.value as ProjectUserManagementScreenState.Success).copy(
                        authorities = authorities,
                    )
                }
            }
        }
    }

    /**
     * removes user authority from given project, if every authority is removed the user will be
     * removed from the project as well, [agreed] must be true to prevent accidental removal of users
     */
    fun deleteAuthority(userAuthority: UserAuthority, agreed: Boolean = false) {
        coroutineScope.launchIO {
            val successState = (mutableState.value as ProjectUserManagementScreenState.Success)

            val authorities = successState.authorities.toMutableList()
            if(!agreed && authorities.filter { it.username == userAuthority.username }.size <= 1) {
                mutableState.update {
                    (mutableState.value as ProjectUserManagementScreenState.Success).copy(
                        dialog = ProjectUserManagementDialog.ConfirmLastAuthorityRemoval(userAuthority)
                    )
                }
            } else if (deleteUserAuthority.await(userAuthority)) {
                authorities.remove(userAuthority)
                mutableState.update {
                    (mutableState.value as ProjectUserManagementScreenState.Success).copy(
                        authorities = authorities,
                    )
                }
            }
        }
    }

    fun showDialog(dialog: ProjectUserManagementDialog) {
        coroutineScope.launchUI {
            mutableState.update {
                (mutableState.value as ProjectUserManagementScreenState.Success).copy(
                    dialog = dialog,
                )
            }
        }
    }

    fun dismissDialog() {
        mutableState.update {
            when (it) {
                is ProjectUserManagementScreenState.Success -> it.copy(dialog = null)
                else -> it
            }
        }
    }
}


sealed class ProjectUserManagementDialog {
    data class ConfirmLastAuthorityRemoval(val userAuthority: UserAuthority) : ProjectUserManagementDialog()
    object AddUserToProject : ProjectUserManagementDialog()
}

sealed class ProjectUserManagementScreenState {

    @Immutable
    object Loading : ProjectUserManagementScreenState()

    @Immutable
    data class Success(
        val projectId: Long,
        val authorities: List<UserAuthority>,
        val dialog: ProjectUserManagementDialog? = null,
    ): ProjectUserManagementScreenState() {
        fun getUsersWithAuthorities(): Map<String, List<UserAuthority>> {
            val authorityMap: UserAuthorityMap = mutableMapOf()
            for(authority in authorities) {
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