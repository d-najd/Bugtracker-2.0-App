package io.dnajd.presentation.project_table.dialogs
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R
import io.dnajd.presentation.components.BugtrackerTextField

@Composable
fun RenameProjectTableDialog(
    originalTitle: String,

    onDismissRequest: () -> Unit,
    onRenameProjectTableClicked: (String) -> Unit,
) {
    // I am aware that storing stuff like this is a bad idea but the alternative is recomposing
    // the entire screen each time a letter is modified
    var newTitle by remember { mutableStateOf(originalTitle) }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = stringResource(R.string.field_rename_column))
        },
        confirmButton = {
            TextButton(
                enabled = newTitle.isNotBlank() && newTitle != originalTitle,
                onClick = {
                    onRenameProjectTableClicked(newTitle)
                }
            ) {
                Text(
                    text = stringResource(R.string.action_rename).uppercase()
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
                value = newTitle,
                onValueChange = { newTitle = it }
            )
        }
    )
}
