package io.dnajd.presentation.project_user_management.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.bugtracker.ui.project_user_management.ProjectUserManagementScreenState
import io.dnajd.domain.project_table_task.model.ProjectTableTask

@Composable
fun ProjectUserManagementContent(
    state: ProjectUserManagementScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
    ) {
        for(authority in state.authorities) {
            ProjectUserManagementItemContent(
                authority = authority,
            )
        }
    }
}
