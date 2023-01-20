package io.dnajd.presentation.project_settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.project_settings.ProjectSettingsScreenState

@Composable
fun ProjectSettingsContent(
    state: ProjectSettingsScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(contentPadding),
    ) {
        Text("Hello")
    }
}