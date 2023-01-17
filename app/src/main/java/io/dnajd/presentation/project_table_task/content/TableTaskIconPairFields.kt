package io.dnajd.presentation.project_table_task.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.project_table_task.components.TableTaskIconPairField
import io.dnajd.util.BugtrackerDateFormat

@Composable
fun TableTaskIconPairFields(
    state: TableTaskScreenState.Success,
){
    val task = state.task

    /* TODO finish this
    TableTaskIconPairField(
        modifier = Modifier.padding(top = 12.dp),
        title = stringResource(R.string.label_labels),
        onClick = { /*TODO*/ }
    ) {

    }
     */

    TableTaskIconPairField(
        modifier = Modifier.padding(top = 16.dp),
        title = stringResource(R.string.field_reporter),
        text = task.reporter,
        iconContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = ""
            )
        }
    )

    TableTaskIconPairField(
        modifier = Modifier.padding(top = 16.dp),
        title = stringResource(R.string.field_created),
        text = BugtrackerDateFormat.defaultRequestDateFormat().format(task.createdAt),
    )

    TableTaskIconPairField(
        modifier = Modifier.padding(top = 16.dp),
        title = stringResource(R.string.field_updated),
        text = if(task.updatedAt != null)
            BugtrackerDateFormat.defaultRequestDateFormat().format(task.updatedAt)
        else "${stringResource(R.string.field_never)} TM",
    )
}