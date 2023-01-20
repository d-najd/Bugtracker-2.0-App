package io.dnajd.bugtracker.ui.base

import androidx.annotation.StringRes
import io.dnajd.bugtracker.R

/**
 * The selected tabs in the second row of the topAppBar, board by default
 */
enum class ProjectTableSelectedTab(@StringRes val titleResId: Int) {
    BOARD(R.string.action_board),
    SETTINGS(R.string.action_settings),
}
