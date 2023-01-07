package io.dnajd.bugtracker.ui.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.library.LibraryScreenContent
import io.dnajd.presentation.util.LocalRouter
import io.dnajd.util.toast
import kotlinx.coroutines.flow.collectLatest

object LibraryScreen : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { LibraryScreenModel(context) }

        val state by screenModel.state.collectAsState()

        if (state is LibraryScreenState.Loading){
            LoadingScreen()
            return
        }

        /**
         * attempt to see if the user has internet connection and if the server is connected before
         * continuing, if not return
         */

        val successState = state as LibraryScreenState.Success

        LibraryScreenContent(
            presenter = successState,
        )

        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                when (event) {
                    is LibraryEvent.LocalizedMessage -> {
                        context.toast(event.stringRes)
                    }
                }
            }
        }
    }
}