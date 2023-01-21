package io.dnajd.bugtracker.ui.project_user_management

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO

class ProjectUserManagementScreenModel(
    context: Context,
    projectId: Long,
) : BugtrackerStateScreenModel<ProjectUserManagementScreenState>(context,
    ProjectUserManagementScreenState.Loading
) {

    init {
        coroutineScope.launchIO {

        }
    }
}


sealed class ProjectUserManagementScreenState {

    @Immutable
    object Loading : ProjectUserManagementScreenState()

    @Immutable
    data class Success(
        val project: Project,
    ): ProjectUserManagementScreenState()

}