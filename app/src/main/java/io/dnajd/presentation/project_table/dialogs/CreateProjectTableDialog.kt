package io.dnajd.presentation.project_table.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.BugtrackerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectDialog(
    onDismissRequest: () -> Unit,
) {
    // I am aware that storing stuff like this is a bad idea but the alternative is recomposing
    // the entire screen each time a letter is modified
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = stringResource(R.string.field_add_column))
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = {
            }) {
                Text(
                    text = stringResource(R.string.action_add).uppercase()
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(R.string.action_cancel).uppercase())
            }
        },
        text = {
            BugtrackerTextField(
                modifierText = Modifier
                    .fillMaxWidth(),
                label = stringResource(R.string.field_project_title),
                title = title,
                onTitleChange = { title = it }
            )

            TextField(
                singleLine = true,
                label = {
                    Text(text = stringResource(R.string.field_column_name))
                },
                value = title,
                onValueChange = { title = it },
            )
        }
    )
}