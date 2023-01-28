package io.dnajd.presentation.project_user_management.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.model.UserAuthorityType
import io.dnajd.presentation.project_user_management.components.ProjectUserManagementAuthoritiesContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserToProjectDialog(
	onDismissRequest: () -> Unit,
	projectId: Long,

	onConfirmClicked: (List<UserAuthority>) -> Unit,
) {
	var username by remember { mutableStateOf("") }
	val authorities = mutableListOf<UserAuthorityType>()

	AlertDialog(
		onDismissRequest = { onDismissRequest() },
		title = {
			Text(text = stringResource(R.string.field_add_user_to_project))
		},
		confirmButton = {
			TextButton(
				onClick = {
					// onConfirmClicked()
				}
			) {
				Text(
					text = stringResource(R.string.action_confirm).uppercase()
				)
			}
		},
		text = {
			Column {
				TextField(
					value = username,
					onValueChange = { username = it }
				)
				ProjectUserManagementAuthoritiesContent(
					projectId = projectId,
					username = username,
					authorities = authorities.map {
						UserAuthority(
							username,
							projectId,
							it
						)
					},
					onInvertAuthorityClicked = { }
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
