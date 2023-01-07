package io.dnajd.bugtracker.ui.library

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import io.dnajd.presentation.util.BugtrackerStateScreenModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryScreenModel(
    private val context: Context,
) : BugtrackerStateScreenModel<LibraryScreenState>(context, LibraryScreenState.Loading) {

    private val _events: Channel<LibraryEvent> = Channel(Int.MAX_VALUE)
    val events: Flow<LibraryEvent> = _events.receiveAsFlow()

    init {
        coroutineScope.launch {
            mutableState.update {
                LibraryScreenState.Success(
                    test = "test",
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
        val test: String? = null,
    ) : LibraryScreenState()

}