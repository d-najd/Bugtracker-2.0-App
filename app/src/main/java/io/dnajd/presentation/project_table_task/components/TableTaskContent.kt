package io.dnajd.presentation.project_table_task.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.components.BugtrackerMultipurposeMenu
import io.dnajd.util.BugtrackerDateFormat

@ExperimentalMaterial3Api
@Composable
fun TableTaskContent(
    state: TableTaskScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 12.dp, vertical = 36.dp),
    ) {
        val task = state.task

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            /* TODO add functionality for this and the other one in the tables screen, when pressed
                the completed state should revert from false to true and vice versa */
            Checkbox(
                modifier = Modifier.size(16.dp),
                checked = true,
                onCheckedChange = { }
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth(),
                text = "${stringResource(R.string.label_task).uppercase()}-${task.id}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
            )
        }

        BasicTextField(
            modifier = Modifier.padding(top = 13.dp),
            value = task.title,
            onValueChange = { },
            textStyle = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Thin,
            ),
            maxLines = 2,
        )

        Card(
            modifier = Modifier
                .padding(top = 18.dp)
                .clickable { },
            shape = RoundedCornerShape(4.dp),
        ) {
            BugtrackerMultipurposeMenu(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 2.dp),
                text = "INSERT PROJECT TITLE",
                includeDropdownArrow = true,
                includeDivider = false,
            )
        }

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.label_description),
            color = MaterialTheme.colorScheme.onSurface.copy(0.65f),
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(70.dp),
            text = stringResource(R.string.info_add_description),
            color = MaterialTheme.colorScheme.onSurface.copy(0.4f),
        )


        // TODO add dropdown menu for child issues and assignees like the dropdown menu for child issues in jira

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
            title = stringResource(R.string.label_reporter),
            text = task.reporter,
        ) {
            Box(
                modifier = Modifier.size(24.dp)
            ) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
            }
        }

        TableTaskIconPairField(
            modifier = Modifier.padding(top = 16.dp),
            title = stringResource(R.string.label_created),
            text = BugtrackerDateFormat.defaultRequestDateFormat().format(task.createdAt),
        )

        TableTaskIconPairField(
            modifier = Modifier.padding(top = 16.dp),
            title = stringResource(R.string.label_updated),
            text = if(task.updatedAt != null)
                BugtrackerDateFormat.defaultRequestDateFormat().format(task.updatedAt)
                else "${stringResource(R.string.label_never)} TM",
        )

        TableTaskActivityContent(state = state)

        // TableTaskBasicInfo(state)
    }
}
