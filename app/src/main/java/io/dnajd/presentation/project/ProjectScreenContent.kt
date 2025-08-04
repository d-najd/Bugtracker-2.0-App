package io.dnajd.presentation.project

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.dnajd.bugtracker.ui.project.ProjectScreenState
import io.dnajd.presentation.project.components.ProjectContent
import io.dnajd.presentation.project.components.ProjectTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreenContent(
	state: ProjectScreenState.Success,

	onProjectClicked: (Long) -> Unit,
	onCreateProjectClicked: () -> Unit,
) {
	/**
	 * If null no filtering, if empty filtering but no string, if string filtering with string
	 */
	var projectFilterString by remember { mutableStateOf<String?>(null) }

	Scaffold(
		topBar = {
			ProjectTopAppBar(
				projectFilterString = projectFilterString,
				onProjectFilterStringChange = { projectFilterString = it },
				onCreateProjectClicked = onCreateProjectClicked,
			)
		},
	) { contentPadding ->
		ProjectContent(
			state = state,
			projectFilterString = projectFilterString,
			contentPadding = contentPadding,
			onProjectClicked = onProjectClicked,
		)
	}
}
