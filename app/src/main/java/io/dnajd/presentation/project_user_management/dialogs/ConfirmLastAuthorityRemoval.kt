package io.dnajd.presentation.project_user_management.dialogs

import androidx.compose.foundation.layout.Column
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

@Composable
fun ConfirmLastAuthorityRemoval(
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = stringResource(R.string.field_remove_user))
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmClicked() }
            ) {
                Text(
                    text = stringResource(R.string.action_confirm).uppercase()
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
            Column {
                Text(
                    text = stringResource(R.string.info_remove_last_authority)
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(R.string.info_do_you_wish_to_continue)
                )
            }
        }
    )
}