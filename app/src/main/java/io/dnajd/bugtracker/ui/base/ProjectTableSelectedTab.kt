package io.dnajd.bugtracker.ui.base

import androidx.annotation.StringRes
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.base.controller.FullComposeController
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsController
import io.dnajd.bugtracker.ui.project_table.ProjectTableController
import io.dnajd.domain.project.model.Project

enum class ProjectTableSelectedTab(@StringRes val titleResId: Int) {
    BOARD(R.string.action_board),
    SETTINGS(R.string.action_settings),
}

fun ProjectTableSelectedTab.getController(
    project: Project,
) : FullComposeController {
    return when(this) {
        ProjectTableSelectedTab.BOARD -> {
            ProjectTableController(project = project)
        }
        ProjectTableSelectedTab.SETTINGS -> {
            ProjectSettingsController(project = project)
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
