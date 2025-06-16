package io.dnajd.presentation.project.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.domain.project.model.Project
import io.dnajd.presentation.components.BugtrackerIconPairField
import io.dnajd.presentation.components.ProjectIconFactory

@Composable
fun ProjectItemContent(
	project: Project,
	onProjectClicked: (Long) -> Unit,
) {
	BugtrackerIconPairField(
		modifier = Modifier
			.padding(horizontal = 12.dp, vertical = 4.dp)
			.fillMaxWidth()
			.height(42.dp)
			.clickable { onProjectClicked(project.id) }, iconContent = {
		Column(
			modifier = Modifier
				.padding(2.dp)
				.background(
					MaterialTheme.colorScheme.onBackground.copy(.75f), RoundedCornerShape(4.dp)
				),
		) {
			Image(
				modifier = Modifier.padding(3.dp),
				painter = painterResource(ProjectIconFactory.getRandom()),
				contentDescription = "",
			)
		}
	}, textContent = {
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
	})
}