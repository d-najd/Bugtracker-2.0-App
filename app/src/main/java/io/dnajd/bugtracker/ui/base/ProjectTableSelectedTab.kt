package io.dnajd.bugtracker.ui.base

import androidx.annotation.StringRes
import cafe.adriel.voyager.core.screen.Screen
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreen
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreen
import io.dnajd.domain.project.model.Project

enum class ProjectTableSelectedTab(@StringRes val titleResId: Int) {
    BOARD(R.string.action_board),
    SETTINGS(R.string.action_settings),
}

// TODO see if this can be refactored or something
fun ProjectTableSelectedTab.getScreen(
    project: Project,
): Screen {
    return when (this) {
        ProjectTableSelectedTab.BOARD -> {
            ProjectTableScreen(project)
        }

        ProjectTableSelectedTab.SETTINGS -> {
            ProjectSettingsScreen(project)
        }
    }
}

/*
/**
 * @param parameters parameters for the controller,
 */
private fun ProjectTableSelectedTab.getController(
    vararg parameters: Any,
) : FullComposeController {
    return when(this) {
        ProjectTableSelectedTab.BOARD -> {
            ProjectTableController(project = parameters[0] as Project)
        }
        ProjectTableSelectedTab.SETTINGS -> {
            ProjectSettingsController(project = parameters[0] as Project)
        }
    }
}
 */
