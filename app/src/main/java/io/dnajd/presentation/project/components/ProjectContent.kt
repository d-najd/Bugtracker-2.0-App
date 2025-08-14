package io.dnajd.presentation.project.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project.ProjectScreenState

@Composable
fun ProjectContent(
	state: ProjectScreenState.Success,
	projectFilterString: String?,
	contentPadding: PaddingValues,
	onProjectClicked: (Long) -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(contentPadding)
			.verticalScroll(rememberScrollState()),
	) {
		val title = if (projectFilterString == null) {
			R.string.field_all_projects
		} else {
			R.string.field_filtered_projects
		}

		Text(
			modifier = Modifier.padding(
				start = 12.dp,
				top = 8.dp,
				bottom = 16.dp,
			),
			text = stringResource(title),
			color = MaterialTheme.colorScheme.onSurface.copy(.5f),
			fontSize = 14.sp,
			fontFamily = FontFamily.SansSerif,
			fontWeight = FontWeight.Medium,
		)

		val projectsFiltered = state.projects()
			.filter { project ->
				@Suppress("NullableBooleanElvis") // more readable like this
				projectFilterString?.let {
					project.title.lowercase()
						.contains(it.lowercase())
				} ?: true
			}

		for (project in projectsFiltered) {
			ProjectItemContent(
				state = state,
				project = project,
				onProjectClicked = onProjectClicked,
			)
		}
	}
}
