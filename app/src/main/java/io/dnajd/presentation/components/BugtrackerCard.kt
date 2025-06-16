package io.dnajd.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @sample BugtrackerCardPreview()
 */
@Composable
fun BugtrackerCard(
	modifier: Modifier = Modifier,
	title: String? = null,
	applyPadding: Boolean = true,
	content: @Composable ColumnScope.() -> Unit,
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.padding(if (applyPadding) ANALYTICS_CARD_PADDING else PaddingValues(0.dp)),
	) {

		Column(
			modifier = Modifier.padding(
				vertical = 6.dp,
				horizontal = 16.dp
			)
		) {
			if (title != null) {
				Text(
					text = title,
					fontSize = 20.sp,
					modifier = Modifier
						.padding(ANALYTICS_CARD_CONTENT_PADDING)
						.padding(vertical = 16.dp),
				)
			}
			content()
		}
	}
}

@Preview(
	widthDp = 300,
)
@Composable
private fun BugtrackerCardPreview() {
	var dismissMenu by remember { mutableStateOf(false) }

	BugtrackerCard(title = "title") {
		BugtrackerPairField(
			key = "key1",
			value = "value1"
		)
		BugtrackerPairField(
			key = "key2",
			value = "value2"
		)
		BugtrackerDropdownMenu(
			onClick = { dismissMenu = !dismissMenu },
			expanded = dismissMenu,
			title = "Top Title",
			text = "Selected Item",
			onDismissRequest = {
				dismissMenu = false
			},
		) {
			DropdownMenuItem(
				text = { Text(text = "Dropdown Item 1") },
				onClick = { dismissMenu = false })
			DropdownMenuItem(
				text = { Text(text = "Dropdown Item 2") },
				onClick = { dismissMenu = false })
			DropdownMenuItem(
				text = { Text(text = "Dropdown Item 3") },
				onClick = { dismissMenu = false })
		}
	}
}

val ANALYTICS_CARD_CONTENT_PADDING = PaddingValues(vertical = 6.dp)
private val ANALYTICS_CARD_PADDING = PaddingValues(
	horizontal = 24.dp,
	vertical = 12.dp
)
