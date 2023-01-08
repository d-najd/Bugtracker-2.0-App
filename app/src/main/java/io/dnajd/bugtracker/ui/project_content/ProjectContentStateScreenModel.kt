package io.dnajd.bugtracker.ui.project_content

import android.content.Context
import androidx.compose.runtime.Immutable
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel

class ProjectScreenModel(
    private val context: Context,

    // private val getProjects: GetProjects = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectScreenState>(context, ProjectScreenState.Loading) {

    /*
    init {
        requestProjectTables(1)
    }
     */

    /*
    private fun requestProjects(username: String) {
        coroutineScope.launchIO {
            val projects = getProjects.await(username)
            mutableState.update {
                ProjectScreenState.Success(
                    projects = projects,
                )
            }
        }
    }
     */

}

sealed class ProjectScreenState {
    
    @Immutable
    object Loading : ProjectScreenState()

    @Immutable
    data class Success(
        val projects: List<Project>,
    ) : ProjectScreenState()

}