package io.dnajd.bugtracker.ui.project_content

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class ProjectContentController: FullComposeController() {

    @Composable
    override fun ComposeContent() {
        Navigator(screen = ProjectContentScreen)
    }

}
