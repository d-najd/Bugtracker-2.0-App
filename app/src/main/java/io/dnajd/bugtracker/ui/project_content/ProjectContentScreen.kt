package io.dnajd.bugtracker.ui.project_content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.currentOrThrow
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.util.LocalRouter

object ProjectContentScreen : Screen {
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

        /*
        LibraryScreenContent(
            presenter = successState,

            //onProjectClicked = { router.setRoot() }
        )
         */

    }
}