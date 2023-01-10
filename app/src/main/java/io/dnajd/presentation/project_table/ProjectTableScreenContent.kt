package io.dnajd.presentation.project_table

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.presentation.project_table.components.ProjectTableContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTableScreenContent(
    state: ProjectTableScreenState.Success,
    onBackClicked: () -> Unit,

    onTableRename: (Long, String) -> Unit,
    onSwitchDropdownMenuClicked: (Int) -> Unit,
) {
    Scaffold { contentPadding ->
        BackHandler { onBackClicked() }

        ProjectTableContent(
            state = state,
            contentPadding = contentPadding,
            onTableRename = onTableRename,
            onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
        )
    }
}