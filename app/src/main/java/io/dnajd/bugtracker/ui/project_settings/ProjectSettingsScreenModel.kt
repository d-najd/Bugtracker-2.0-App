package io.dnajd.bugtracker.ui.project_settings

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.interactor.GetProject
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project_table.interactor.*
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectSettingsScreenModel(
    context: Context,
    val project: Project,

    private val getProject: GetProject = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectSettingsScreenState>(context,
    ProjectSettingsScreenState.Loading(project)
) {
    private val _events: Channel<ProjectSettingsEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<ProjectSettingsEvent> = _events.receiveAsFlow()

    init {
        coroutineScope.launchIO {
            mutableState.update {
                ProjectSettingsScreenState.Success(
                    project = project,
                )
            }
            val persistedProject = getProject.awaitOne(project.id)
            if(persistedProject != null) {
                // it does not produce right results with binary operator
                @Suppress("ReplaceCallWithBinaryOperator")
                if(persistedProject.equals(project)) {
                    mutableState.update {
                        ProjectSettingsScreenState.Success(
                            project = persistedProject,
                        )
                    }
                    _events.send(ProjectSettingsEvent.ProjectModified(persistedProject))
                }
            } else {
                _events.send(ProjectSettingsEvent.InvalidProjectId)
            }
        }
    }
}

sealed class ProjectSettingsEvent {
    sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectSettingsEvent()

    object InvalidProjectId : LocalizedMessage(R.string.error_invalid_project_id)
    data class ProjectModified(val project: Project) : ProjectSettingsEvent()
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