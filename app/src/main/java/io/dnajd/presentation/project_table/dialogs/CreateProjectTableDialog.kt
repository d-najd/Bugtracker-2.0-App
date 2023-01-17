package io.dnajd.presentation.project_table.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.components.BugtrackerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectTableDialog(
    onDismissRequest: () -> Unit,
    onCreateTableClicked: (ProjectTable) -> Unit,
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
                    // TODO finish this
                    /*
                    val table = ProjectTable(
                        title = title,
                    )
                     */
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
                label = stringResource(R.string.field_column_name),
                title = title,
                onTitleChange = { title = it }
            )
        }
    )
}