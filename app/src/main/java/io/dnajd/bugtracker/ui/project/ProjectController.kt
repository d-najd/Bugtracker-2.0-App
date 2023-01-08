package io.dnajd.bugtracker.ui.project

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class ProjectController: FullComposeController() {

    @Composable
    override fun ComposeContent() {
        Navigator(screen = ProjectScreen)
    }

}
