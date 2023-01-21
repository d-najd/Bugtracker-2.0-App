package io.dnajd.bugtracker.ui.project_details

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.interactor.DeleteProject
import io.dnajd.domain.project.interactor.RenameProject
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectDetailsScreenModel(
    context: Context,
    project: Project,

    private val deleteProject: DeleteProject = Injekt.get(),
    private val renameProject: RenameProject = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectDetailsScreenState>(context,
    ProjectDetailsScreenState.Loading
) {
    private val _events: Channel<ProjectDetailsEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<ProjectDetailsEvent> = _events.receiveAsFlow()

    init {
        coroutineScope.launch {
            mutableState.update {
                ProjectDetailsScreenState.Success(
                    project = project,
                )
            }
        }
    }

    fun deleteProject() {
        coroutineScope.launch {
            (mutableState.value as ProjectDetailsScreenState.Success).project.let { project ->
                if(deleteProject.await(project.id)) {
                    _events.send(ProjectDetailsEvent.DeleteProject)
                }
            }
        }
    }

    fun renameProject(title: String) {
        coroutineScope.launchIO {
            (mutableState.value as ProjectDetailsScreenState.Success).project.let { persistedProject ->
                if(renameProject.await(id = persistedProject.id, newTitle = title)) {
                    val renamedProject = persistedProject.copy(
                        title = title,
                    )
                    mutableState.update {
                        (mutableState.value as ProjectDetailsScreenState.Success).copy(
                            project = renamedProject,
                        )
                    }
                    _events.send(ProjectDetailsEvent.ProjectRenamed(renamedProject))
                }
            }
        }
    }
}


sealed class ProjectDetailsEvent {
    object DeleteProject : ProjectDetailsEvent()
    data class ProjectRenamed(val project: Project) : ProjectDetailsEvent()
}

sealed class ProjectDetailsScreenState {

    @Immutable
    object Loading : ProjectDetailsScreenState()

    @Immutable
    data class Success(
        val project: Project,
    ): ProjectDetailsScreenState()

}