package io.dnajd.bugtracker.ui.util

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