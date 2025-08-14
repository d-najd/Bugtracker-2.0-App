package io.dnajd.presentation.project.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.ui.project.ProjectScreenState
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.BugtrackerIconPairField

@Composable
fun ProjectItemContent(
	state: ProjectScreenState.Success,

	project: Project,
	onProjectClicked: (Long) -> Unit,
) {
	BugtrackerIconPairField(
		modifier = Modifier
			.padding(
				horizontal = 12.dp,
				vertical = 4.dp,
			)
			.fillMaxWidth()
			.height(42.dp)
			.clickable { onProjectClicked(project.id) },
		iconContent = {
			Card(
				modifier = Modifier
					.size(40.dp)
					.padding(2.dp),
				shape = RoundedCornerShape(4.dp),
				colors = CardDefaults.cardColors()
					.copy(
						containerColor = MaterialTheme.colorScheme.onBackground.copy(.75f),
						contentColor = MaterialTheme.colorScheme.onBackground.copy(.75f),
					),
				border = BorderStroke(0.dp, MaterialTheme.colorScheme.background),
			) {
				val projectIcon = state.projectIcons()
					.first { it.projectId == project.id }

				Image(
					contentScale = ContentScale.Fit,
					bitmap = projectIcon.bitmap.asImageBitmap(),
					contentDescription = null,
				)
			}
		},
		textContent = {
			Text(
				modifier = Modifier.padding(start = 2.dp),
				text = project.title,
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = (13.75).sp,
				fontFamily = FontFamily.SansSerif,
				fontWeight = FontWeight.Light,
			)

			Text(
				modifier = Modifier.padding(start = 2.dp),
				text = "owner: ${project.owner}",
				color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
				fontSize = 12.sp,
				fontWeight = FontWeight.ExtraLight,
				fontFamily = FontFamily.SansSerif,
			)
		},
	)
}
