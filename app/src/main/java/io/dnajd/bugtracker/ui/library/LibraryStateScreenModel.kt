package io.dnajd.bugtracker.ui.library

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.domain.project.interactor.GetProjects
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

class LibraryScreenModel(
    private val context: Context,

    private val getProjects: GetProjects = Injekt.get(),
) : BugtrackerStateScreenModel<LibraryScreenState>(context, LibraryScreenState.Loading) {

    private val _events: Channel<LibraryEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<LibraryEvent> = _events.receiveAsFlow()

    init {
        requestProjects("user1")
    }

    private fun requestProjects(username: String) {
        coroutineScope.launchIO {
            val projects = getProjects.await(username)
            mutableState.update {
                LibraryScreenState.Success(
                    projects = projects,
                )
            }
        }
    }

    fun showLocalizedEvent(event: LibraryEvent.LocalizedMessage) {
        coroutineScope.launch{
            _events.send(event)
        }
    }
}

sealed class LibraryEvent {
    sealed class LocalizedMessage(@StringRes val stringRes: Int) : LibraryEvent()
}

sealed class LibraryScreenState {
    
    @Immutable
    object Loading : LibraryScreenState()

    @Immutable
    data class Success(
        val projects: List<Project>,
    ) : LibraryScreenState()

}