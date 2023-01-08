package io.dnajd.bugtracker.ui.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project.ProjectScreenContent
import io.dnajd.presentation.util.LocalRouter

object ProjectScreen : Screen {
    @Composable
    override fun Content() {
        // val navigator = LocalNavigator.currentOrThrow
        val router = LocalRouter.currentOrThrow
        val context = LocalContext.current
        val screenModel = rememberScreenModel { ProjectScreenModel(context) }

        val state by screenModel.state.collectAsState()

        if (state is ProjectScreenState.Loading){
            LoadingScreen()
            return
        }

        /**
         * attempt to see if the user has internet connection and if the server is connected before
         * continuing, if not return
         */

        val successState = state as ProjectScreenState.Success

        ProjectScreenContent(
            presenter = successState,

            //onProjectClicked = { router.setRoot() }
        )

        /*
        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                when (event) {
                    is LibraryEvent.LocalizedMessage -> {
                        context.toast(event.stringRes)
                    }
                }
            }
        }
         */
    }
}