package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState

@Composable
fun TableTaskDescriptionField(
    state: TableTaskScreenState.Success,
    onAlterDescriptionSheetClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onAlterDescriptionSheetClicked() }
    ) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(R.string.field_description),
            color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(92.dp),
            fontSize = (14.5).sp,
            text = state.task.description ?: stringResource(R.string.action_tap_to_add_description),
            color = if(state.task.description != null) Color.Unspecified
                else MaterialTheme.colorScheme.onSurface.copy(0.4f),
            maxLines = 3,
            minLines = 1,
        )
    }
}