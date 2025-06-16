package io.dnajd.presentation.user_management.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R

@Composable
fun ConfirmLastAuthorityRemovalDialog(
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
				onClick = { onConfirmClicked() }) {
				Text(
					text = stringResource(R.string.action_confirm).uppercase()
				)
			}
		},
		dismissButton = {
			TextButton(
				onClick = { onDismissRequest() }) {
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
		})
}