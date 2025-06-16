package io.dnajd.presentation.project.dialogs

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
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.BugtrackerTextField

@Composable
fun CreateProjectDialog(
	onDismissRequest: () -> Unit,
	onCreateProjectClicked: (Project) -> Unit,
) {
	// I am aware that storing stuff like this is a bad idea but the alternative is recomposing
	// the entire screen each time a letter is modified
	var title by remember { mutableStateOf("") }

	AlertDialog(onDismissRequest = { onDismissRequest() }, title = {
		Text(text = stringResource(R.string.field_create_project))
	}, confirmButton = {
		TextButton(
			enabled = title.isNotBlank(), onClick = {
				val project = Project(
					owner = "user1",
					title = title,
				)
				onCreateProjectClicked(project)
			}) {
			Text(
				text = stringResource(R.string.action_create).uppercase()
			)
		}
	}, dismissButton = {
		TextButton(
			onClick = { onDismissRequest() }) {
			Text(text = stringResource(R.string.action_cancel).uppercase())
		}
	}, text = {
		BugtrackerTextField(
			modifierText = Modifier.fillMaxWidth(),
			label = stringResource(R.string.field_project_title),
			value = title,
			onValueChange = { title = it })
	})
}