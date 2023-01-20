package io.dnajd.bugtracker.ui.project_settings

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.interactor.*
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectSettingsScreenModel(
    context: Context,
    project: Project,
) : BugtrackerStateScreenModel<ProjectSettingsScreenState>(context,
    ProjectSettingsScreenState.Loading(project)
) {
    init {
        coroutineScope.launch {
            mutableState.update {
                ProjectSettingsScreenState.Success(
                    project = project,
                )
            }
        }
    }
}

sealed class ProjectSettingsScreenState(
    open val project: Project,
) {
    
    @Immutable
    data class Loading(
        override val project: Project,
    ) : ProjectSettingsScreenState(project)

    @Immutable
    data class Success(
        override val project: Project,
    ): ProjectSettingsScreenState(project)

}