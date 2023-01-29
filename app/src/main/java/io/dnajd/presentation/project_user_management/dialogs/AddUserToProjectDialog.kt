package io.dnajd.presentation.project_user_management.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserToProjectDialog(
	onDismissRequest: () -> Unit,
	projectId: Long,

	onConfirmClicked: (UserAuthority) -> Unit,
) {
	var username by remember { mutableStateOf("") }
	// val authorities = mutableListOf<UserAuthorityType>()

	AlertDialog(
		onDismissRequest = { onDismissRequest() },
		title = {
			Text(text = stringResource(R.string.field_add_user_to_project))
		},
		text = {
			Column {
				Text(text = "Adds user with view authority to the project")

				OutlinedTextField(
					modifier = Modifier.padding(top = 8.dp),
					label = { Text(text = stringResource(R.string.field_username)) },
					value = username,
					onValueChange = { username = it }
				)
			}
		},
		confirmButton = {
			TextButton(
				onClick = {
					onConfirmClicked(
						UserAuthority(
							username = username,
							projectId = projectId,
							authority = UserAuthorityType.project_view,
						)
					)
				}
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
	)
}
