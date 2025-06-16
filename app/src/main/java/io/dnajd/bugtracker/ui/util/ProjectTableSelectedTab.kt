package io.dnajd.bugtracker.ui.util

import androidx.annotation.StringRes
import cafe.adriel.voyager.core.screen.Screen
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreen
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreen

enum class ProjectTableSelectedTab(@StringRes val titleResId: Int) {
	BOARD(R.string.action_board),
	SETTINGS(R.string.action_settings),
}

// TODO see if this can be refactored or something
fun ProjectTableSelectedTab.getScreen(
	projectId: Long,
): Screen {
	return when (this) {
		ProjectTableSelectedTab.BOARD -> {
			ProjectTableScreen(projectId)
		}

		ProjectTableSelectedTab.SETTINGS -> {
			ProjectSettingsScreen(projectId)
		}
	}
}