package io.dnajd.presentation.project_settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState

@Composable
fun ProjectSettingsContent(
    state: ProjectSettingsScreenState.Success,
    contentPadding: PaddingValues,

    onProjectDetailsClicked: () -> Unit,
    onUserManagementClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 14.dp),
    ) {
        ProjectSettingsItem(
            title = stringResource(R.string.action_details),
            onClick = { onProjectDetailsClicked() }
        )
        ProjectSettingsItem(
            title = stringResource(R.string.action_user_management),
            onClick = { onUserManagementClicked() }
        )
    }
}

@Composable
fun ProjectSettingsItem(
    title: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = title,
                fontSize = (16.25).sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
    Divider()
}
