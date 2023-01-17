package io.dnajd.presentation.project_table_task.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import io.dnajd.presentation.project_table_task.content.comment.TableTaskActivityContent

@ExperimentalMaterial3Api
@Composable
fun TableTaskContent(
    state: TableTaskScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                text = "${stringResource(R.string.field_task).uppercase()}-${task.id}",
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
            shape = RoundedCornerShape(6.dp),
        ) {
            BugtrackerMultipurposeMenu(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 2.dp),
                text = {
                    Text(
                        modifier = Modifier.padding(start = 3.5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        text = "INSERT PROJECT TITLE",
                    )
                },
                includeDropdownArrow = true,
                includeDivider = false,
            )
        }

        TableTaskDescriptionField(state = state)

        TableTaskChildIssuesField(state = state)

        TableTaskAssignedField(state = state)

        TableTaskIconPairFields(state = state)

        TableTaskActivityContent(state = state)

        // TableTaskBasicInfo(state)
    }
}
