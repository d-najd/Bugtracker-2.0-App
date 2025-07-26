package io.dnajd.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R

/**
 * @param modifier modifier for the composable
 * @param title title located on top of [text]
 * @param titleContent same principle as [title] but offers more flexibility
 * @param iconContent "icon content", this is optional content located to the left of [text]
 * @param text optional text located right of the [textContent]
 * @param textContent same principle as [text] but offers more flexibility
 * @throws IllegalArgumentException if [title] and [titleContent] are both set or if [text] and
 *  [textContent] are both set
 * @sample BugtrackerIconPairFieldPreview()
 */
@Composable
fun BugtrackerIconPairField(
	modifier: Modifier = Modifier,
	title: String? = null,
	titleContent: (@Composable ColumnScope.() -> Unit)? = null,
	iconContent: (@Composable RowScope.() -> Unit)? = null,
	text: String? = null,
	textContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
	if (title != null && titleContent != null) {
		throw IllegalArgumentException("title or titleContent must be null")
	}
	if (text != null && textContent != null) {
		throw IllegalArgumentException("text or textContent must be null")
	}

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
	) {
		if (title != null) {
			Text(
				text = title,
				color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
			)
		}
		titleContent?.let { titleContent() }

		Row(
			modifier = Modifier.padding(top = if (title != null) 6.dp else 0.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			iconContent?.let { iconContent() }

			val textPadding = if ((textContent != null || text != null) && iconContent != null) 8.dp else 0.dp
			Column(
				modifier = Modifier.padding(start = textPadding),
				verticalArrangement = Arrangement.Center,
			) {
				textContent?.let { textContent() }
				if (text != null) {
					Text(
						text = text,
						fontSize = 17.sp,
						fontWeight = FontWeight.Thin,
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun BugtrackerIconPairFieldPreview() {
	Column {
		BugtrackerCard {
			BugtrackerIconPairField(
				modifier = Modifier
					.fillMaxWidth()
					.clickable { },
				title = "Reporter",
				text = "user1",
				iconContent = {
					Icon(
						modifier = Modifier.size(24.dp),
						imageVector = Icons.Default.AccountCircle,
						contentDescription = "",
					)
				},
			)
		}
		BugtrackerCard {
			BugtrackerIconPairField(
				iconContent = {
					Icon(
						imageVector = Icons.Default.TaskAlt,
						tint = MaterialTheme.colorScheme.primary,
						contentDescription = "",
					)
				},
				textContent = {
					Text(text = "Child Task")
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						Text(
							text = "${stringResource(R.string.field_task).uppercase()}-${1}",
							color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
						)
						Text(
							text = " = ",
							color = colorResource(R.color.coral),
							fontFamily = FontFamily.SansSerif,
							fontSize = 26.sp,
						)
						Text(
							text = "Table 1",
							color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
						)
					}
				},
			)
		}
	}
}
