package io.dnajd.presentation.project_table_task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState

@Composable
fun TableTaskAlterDescriptionContent(
	state: TableTaskScreenState.AlterTaskDescription,
) {
	BasicTextField(
		modifier = Modifier.fillMaxSize(),
		value = "",
		onValueChange = {},
	)
}