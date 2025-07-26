package io.dnajd.presentation.project_table.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.presentation.components.BugtrackerTextField

@Composable
fun CreateProjectTableDialog(
	state: ProjectTableScreenState.Success,

	placeholderTitle: String,
	onDismissRequest: () -> Unit,
	onCreateTableClicked: (ProjectTable) -> Unit,
) {    // I am aware that storing stuff like this is a bad idea but the alternative is recomposing
	// the entire screen each time a letter is modified
	var title by remember { mutableStateOf(placeholderTitle) }

	AlertDialog(
		onDismissRequest = { onDismissRequest() },
		title = {
			Text(text = stringResource(R.string.field_add_column))
		},
		confirmButton = {
			TextButton(
				enabled = title.isNotBlank(),
				onClick = {
					onCreateTableClicked(
						ProjectTable(
							projectId = state.projectId,
							title = title,
						),
					)
				},
			) {
				Text(
					text = stringResource(R.string.action_add).uppercase(),
				)
			}
		},
		dismissButton = {
			TextButton(
				onClick = { onDismissRequest() },
			) {
				Text(text = stringResource(R.string.action_cancel).uppercase())
			}
		},
		text = {
			BugtrackerTextField(
				modifierText = Modifier.fillMaxWidth(),
				label = stringResource(R.string.field_column_name),
				value = title,
				onValueChange = { title = it },
			)
		},
	)
}
