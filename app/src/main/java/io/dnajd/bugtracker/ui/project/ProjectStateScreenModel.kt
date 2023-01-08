package io.dnajd.bugtracker.ui.project

import android.content.Context
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.interactor.GetProjects
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import io.dnajd.util.launchIO
import kotlinx.coroutines.flow.update
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class ProjectScreenModel(
    private val context: Context,

    private val getProjects: GetProjects = Injekt.get(),
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

    /*
    fun showLocalizedEvent(event: LibraryEvent.LocalizedMessage) {
        coroutineScope.launch{
            _events.send(event)
        }
    }
     */
}

/*
sealed class LibraryEvent {
    sealed class LocalizedMessage(@StringRes val stringRes: Int) : LibraryEvent()
}
 */

sealed class ProjectScreenState {
    
    @Immutable
    object Loading : ProjectScreenState()

    @Immutable
    data class Success(
        val projects: List<Project>,
    ) : ProjectScreenState()

}