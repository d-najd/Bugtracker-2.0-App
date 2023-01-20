package io.dnajd.bugtracker.ui.project_details

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectDetailsScreenModel(
    context: Context,
    project: Project,
) : BugtrackerStateScreenModel<ProjectDetailsScreenState>(context,
    ProjectDetailsScreenState.Loading
) {
    init {
        coroutineScope.launch {
            mutableState.update {
                ProjectDetailsScreenState.Success(
                    project = project,
                )
            }
        }
    }
}

sealed class ProjectDetailsScreenState {
    
    @Immutable
    object Loading : ProjectDetailsScreenState()

    @Immutable
    data class Success(
        val project: Project,
    ): ProjectDetailsScreenState()

}