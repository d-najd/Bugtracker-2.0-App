package io.dnajd.bugtracker.ui.project_table

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import cafe.adriel.voyager.navigator.Navigator
import io.dnajd.bugtracker.ui.base.controller.FullComposeController

class ProjectTableController: FullComposeController {

    constructor(projectId: Long) : super(
        bundleOf(
            PROJECT_ID_EXTRA to projectId,
        ),
    )

    // the constructor is necessary, removing it will result in a crash
    @Suppress("unused")
    constructor(bundle: Bundle) : this(
        bundle.getLong(PROJECT_ID_EXTRA),
    )

    val projectId: Long
        get() = args.getLong(PROJECT_ID_EXTRA)

    @Composable
    override fun ComposeContent() {
        Navigator(screen = ProjectTableScreen(projectId))
    }


    companion object{
        const val PROJECT_ID_EXTRA = "projectId"
    }

}
