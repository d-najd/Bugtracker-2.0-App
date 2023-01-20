package io.dnajd.presentation.project_table

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.base.ProjectTableSelectedTab
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table.components.ProjectTableContent
import io.dnajd.presentation.project_table.components.ProjectTableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTableScreenContent(
    state: ProjectTableScreenState,
    onBackClicked: () -> Unit,

    onTableRename: (Long, String) -> Unit,
    onMoveTableTasks: (Long, Int, Int) -> Unit,
    onDeleteTableClicked: (Long) -> Unit,
    onCreateTableClicked: () -> Unit,
    onCreateTableTaskMenuClicked: (Long?) -> Unit,
    onCreateTableTaskClicked: (ProjectTableTask) -> Unit,
    onTaskClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Long?) -> Unit,
    onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
    Scaffold(
        topBar = {
            ProjectTableTopAppBar(
                state = state,
                onBackClicked = onBackClicked,
                onCreateTableClicked = onCreateTableClicked,
                onSwitchScreenTabClicked = onSwitchScreenTabClicked,
            )
        }
    ) { contentPadding ->
        BackHandler { onBackClicked() }

        if (state is ProjectTableScreenState.Loading) {
            LoadingScreen()
            return@Scaffold
        }

        val successState = state as ProjectTableScreenState.Success

        ProjectTableContent(
            state = successState,
            contentPadding = contentPadding,
            onTableRename = onTableRename,
            onMoveTableTasks = onMoveTableTasks,
            onDeleteTableClicked = onDeleteTableClicked,
            onCreateTableTaskMenuClicked = onCreateTableTaskMenuClicked,
            onCreateTableTaskClicked = onCreateTableTaskClicked,
            onTaskClicked = onTaskClicked,
            onSwapTablePositionsClicked = onSwapTablePositionsClicked,
            onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
        )
    }
}