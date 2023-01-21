package io.dnajd.bugtracker.ui.project_user_management

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class ProjectUserManagementController: FullComposeController {
    constructor(projectId: Long) : super(
        bundleOf(
            PROJECT_ID_EXTRA to projectId
        )
    )

    // the constructor is necessary, removing it will result in a crash
    @Suppress("unused")
    constructor(bundle: Bundle) : this(
        bundle.getLong(PROJECT_ID_EXTRA),
    )

    private val projectId: Long
        get() = args.getLong(PROJECT_ID_EXTRA)

    @Composable
    override fun ComposeContent() {
        Navigator(screen = ProjectUserManagementScreen(projectId))
    }

    companion object{
        const val PROJECT_ID_EXTRA = "projectId"
    }

}
