package io.dnajd.presentation.project_table

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.presentation.project_table.components.ProjectTableContent
import io.dnajd.presentation.project_table.components.ProjectTableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTableScreenContent(
    state: ProjectTableScreenState.Success,
    onBackClicked: () -> Unit,

    onTableRename: (Long, String) -> Unit,
    onMoveTableTasks: (Long, Int, Int) -> Unit,
    onDeleteTableClicked: (Long) -> Unit,
    onCreateTableClicked: () -> Unit,
    onCreateTableTaskMenuClicked: (Long) -> Unit,
    onTaskClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Long?) -> Unit,
) {
    Scaffold(
        topBar = {
            ProjectTableTopAppBar(
                state = state,
                onBackClicked = onBackClicked,
                onCreateTableClicked = onCreateTableClicked,
            )
        }
    ) { contentPadding ->
        BackHandler { onBackClicked() }

        ProjectTableContent(
            state = state,
            contentPadding = contentPadding,
            onTableRename = onTableRename,
            onMoveTableTasks = onMoveTableTasks,
            onDeleteTableClicked = onDeleteTableClicked,
            onCreateTableTaskMenuClicked = onCreateTableTaskMenuClicked,
            onTaskClicked = onTaskClicked,
            onSwapTablePositionsClicked = onSwapTablePositionsClicked,
            onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
        )
    }
}