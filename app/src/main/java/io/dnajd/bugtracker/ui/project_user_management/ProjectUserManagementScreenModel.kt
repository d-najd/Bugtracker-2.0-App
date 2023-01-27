package io.dnajd.bugtracker.ui.project_user_management

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.user_authority.interactor.GetUserAuthorities
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.function.BiFunction

typealias UserAuthorityMap = MutableMap<String, List<UserAuthority>>

class ProjectUserManagementScreenModel(
    context: Context,
    projectId: Long,

    private val getUserAuthorities: GetUserAuthorities = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectUserManagementScreenState>(context,
    ProjectUserManagementScreenState.Loading
) {
    init {
        coroutineScope.launchIO {
            val authorities = getUserAuthorities.await(projectId)
            if(authorities.isNotEmpty()) {
                mutableState.update {
                    ProjectUserManagementScreenState.Success(
                        authorities = authorities,
                    )
                }
            }
        }
    }


}

sealed class ProjectUserManagementScreenState {

    @Immutable
    object Loading : ProjectUserManagementScreenState()

    @Immutable
    data class Success(
        val authorities: List<UserAuthority>,
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
            return authorityMap
        }
    }

}