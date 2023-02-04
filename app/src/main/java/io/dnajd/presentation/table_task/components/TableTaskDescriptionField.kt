package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState

@Composable
fun TableTaskDescriptionField(
    state: TableTaskScreenState.Success,
) {
    Text(
        modifier = Modifier.padding(top = 28.dp),
        text = stringResource(R.string.field_description),
        color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
    )

    Text(
        modifier = Modifier
            .padding(top = 8.dp)
            .height(92.dp),
        text = stringResource(R.string.info_add_description),
        color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
    )
}