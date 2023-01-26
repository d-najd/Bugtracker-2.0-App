package io.dnajd.presentation.project_table_task.components.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState

@Composable
fun TableTaskActivityContent(state: TableTaskScreenState.Success){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            fontSize = (16.5).sp,
            color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
            text = "${stringResource(R.string.field_activity)}:",
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.field_comments),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
            )

            Icon(
                modifier = Modifier,
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = ""
            )
        }
    }

    if(state.task.comments.isEmpty()) {
        TableTaskNoCommentsContent()
    } else {
        TableTaskCommentsContent(state = state)
    }
}