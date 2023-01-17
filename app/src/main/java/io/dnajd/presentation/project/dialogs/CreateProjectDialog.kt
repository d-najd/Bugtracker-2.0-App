package io.dnajd.presentation.project.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R
import io.dnajd.domain.project.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectDialog(
    onDismissRequest: () -> Unit,
    onCreateProjectClicked: (Project) -> Unit,
) {
    // I am aware that storing stuff like this is a bad idea but the alternative is recomposing
    // the entire screen each time a letter is modified
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = stringResource(R.string.field_create_project))
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank(),
                onClick = {
                val project = Project.apiBase(
                    owner = "user1",
                    title = title,
                )
                onCreateProjectClicked(project)
            }) {
                Text(
                    text = stringResource(R.string.action_create).uppercase()
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
            TextField(
                singleLine = true,
                label = {
                    Text(text = stringResource(R.string.field_project_title))
                },
                value = title,
                onValueChange = { title = it },
            )
        }
    )
}