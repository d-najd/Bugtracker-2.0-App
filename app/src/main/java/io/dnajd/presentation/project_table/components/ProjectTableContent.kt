package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState

@Composable
fun ProjectTableContent(
    state: ProjectTableScreenState.Success,
    contentPadding: PaddingValues,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
    ) {
        for ((index, projectTable) in state.projectTables.sortedBy { it.position }.withIndex()) {
            ProjectTableCard(
                projectTable = projectTable,
                projectTableLength = state.projectTables.size,
                index = index,
            )
        }
    }


}
