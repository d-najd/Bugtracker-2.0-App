package io.dnajd.bugtracker.ui.library

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class LibraryController: FullComposeController() {

    @Composable
    override fun ComposeContent() {
        Navigator(screen = LibraryScreen)
    }

}
