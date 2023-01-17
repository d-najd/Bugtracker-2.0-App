package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState

@Composable
fun ProjectTableContent(
    state: ProjectTableScreenState.Success,
    contentPadding: PaddingValues,

    onTableRename: (Long, String) -> Unit,
    onMoveTableTasks: (Long, Int, Int) -> Unit,
    onTaskClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(5.dp),
    ) {
        for ((index, projectTable) in state.tables.sortedBy { it.position }.withIndex()) {
            ProjectTableCard(
                state = state,
                projectTable = projectTable,
                index = index,
                onTableRename = onTableRename,
                onMoveTableTasks = onMoveTableTasks,
                onTaskClicked = onTaskClicked,
                onSwapTablePositionsClicked = onSwapTablePositionsClicked,
                onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
            )
        }
    }
}
