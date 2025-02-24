package io.dnajd.bugtracker.ui.project_details

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.interactor.DeleteProject
import io.dnajd.domain.project.interactor.GetProject
import io.dnajd.domain.project.interactor.RenameProject
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectDetailsScreenModel(
    context: Context,
    project: Project,

    // TODO this is horrible move this to th viewmodel
    private val getProject: GetProject = Injekt.get(),
    private val deleteProject: DeleteProject = Injekt.get(),
    private val renameProject: RenameProject = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectDetailsScreenState>(context,
    ProjectDetailsScreenState.Loading
) {
    private val _events: Channel<ProjectDetailsEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<ProjectDetailsEvent> = _events.receiveAsFlow()

    init {
        coroutineScope.launchIO {
            val persistedProject = getProject.awaitOne(project.id)
            if(persistedProject != null) {
                mutableState.update {
                    ProjectDetailsScreenState.Success(
                        project = persistedProject,
                    )
                }
            } else {
                _events.send(ProjectDetailsEvent.InvalidProjectId)
            }
        }
    }

    fun deleteProject() {
        coroutineScope.launchIO {
            (mutableState.value as ProjectDetailsScreenState.Success).project.let { project ->
                if(deleteProject.await(project.id)) {
                    _events.send(ProjectDetailsEvent.DeleteProject)
                }
            }
        }
    }

    fun renameProject(title: String) {
        coroutineScope.launchIO {
            (mutableState.value as ProjectDetailsScreenState.Success).project.let { transientProject ->
                if(renameProject.await(id = transientProject.id, newTitle = title)) {
                    val renamedProject = transientProject.copy(
                        title = title,
                    )
                    mutableState.update {
                        (mutableState.value as ProjectDetailsScreenState.Success).copy(
                            project = renamedProject,
                        )
                    }
                    _events.send(ProjectDetailsEvent.ProjectModified(renamedProject))
                }
            }
        }
    }
}


sealed class ProjectDetailsEvent {
    sealed class LocalizedMessage(@StringRes val stringRes: Int) : ProjectDetailsEvent()

    object InvalidProjectId : LocalizedMessage(R.string.error_invalid_project_id)
    object DeleteProject : ProjectDetailsEvent()
    data class ProjectModified(val project: Project) : ProjectDetailsEvent()
}

sealed class ProjectDetailsScreenState {

    @Immutable
    object Loading : ProjectDetailsScreenState()

    @Immutable
    data class Success(
        val project: Project,
    ): ProjectDetailsScreenState()

}