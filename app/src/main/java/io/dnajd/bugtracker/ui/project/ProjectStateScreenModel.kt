package io.dnajd.bugtracker.ui.project

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.interactor.CreateProject
import io.dnajd.domain.project.interactor.GetProjects
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import io.dnajd.util.launchUI
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectScreenModel(
    context: Context,

    private val getProjects: GetProjects = Injekt.get(),
    private val createProject: CreateProject = Injekt.get(),
) : BugtrackerStateScreenModel<ProjectScreenState>(context, ProjectScreenState.Loading) {

    /*
    private val _events: Channel<LibraryEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<LibraryEvent> = _events.receiveAsFlow()
     */

    init {
        requestProjects("user1")
    }

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

    fun createProject(project: Project) {
        coroutineScope.launchIO {
            createProject.awaitOne(project)?.let { persistedProject ->
                mutableState.update {
                    val projects = (mutableState.value as ProjectScreenState.Success).projects.toMutableList()
                    projects.add(persistedProject)
                    (mutableState.value as ProjectScreenState.Success).copy(
                        projects = projects
                    )
                }
            }
            dismissDialog()
        }
    }

    fun showDialog(dialog: ProjectDialog) {
        when (dialog) {
            is ProjectDialog.CreateProject -> {
                coroutineScope.launchUI {
                    mutableState.update {
                        (mutableState.value as ProjectScreenState.Success).copy(
                            dialog = dialog,
                        )
                    }
                }
            }
        }
    }

    fun dismissDialog() {
        mutableState.update {
            when (it) {
                is ProjectScreenState.Success -> it.copy(dialog = null)
                else -> it
            }
        }
    }

    /*
    fun showLocalizedEvent(event: LibraryEvent.LocalizedMessage) {
        coroutineScope.launch{
            _events.send(event)
        }
    }
     */
}

sealed class ProjectDialog {
    data class CreateProject(val title: String = "") : ProjectDialog()
}


sealed class ProjectScreenState {
    
    @Immutable
    object Loading : ProjectScreenState()

    @Immutable
    data class Success(
        val projects: List<Project>,
        val dialog: ProjectDialog? = null,
    ) : ProjectScreenState()

}