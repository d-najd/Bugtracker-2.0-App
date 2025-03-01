package io.dnajd.presentation.table_task.sheets

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table.model.ProjectTable

/**
 * bottom sheet used for changing the table of the task
 */
@Composable
fun TableTaskBottomSheetContent(
	curTable: ProjectTable,
	tables: List<ProjectTable>,
	onChangeTableClicked: (Long) -> Unit,
) {
	SheetItem(
		title = stringResource(R.string.field_select_transition),
		textColor = MaterialTheme.colorScheme.onSurface
	)

	val tablesSorted = tables.toMutableList()
	if (!tablesSorted.remove(tablesSorted.find { it.id == curTable.id })) {
		throw IllegalStateException()
	}
	tablesSorted.add(0, curTable)

	for (table in tablesSorted) {
		SheetItem(
			title = table.title,
			onClick = { onChangeTableClicked(table.id) }
		)
	}

}

@SuppressLint("UnnecessaryComposedModifier")
@Composable
private fun SheetItem(
	title: String,
	textColor: Color = MaterialTheme.colorScheme.onSurface,
	onClick: (() -> Unit)? = null,
) {
	Column(
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxWidth()
			.height(38.dp)
			.composed {
				if (onClick != null) {
					return@composed clickable {
						onClick()
					}
				}
				this
			},
	) {
		Text(
			modifier = Modifier.padding(start = 16.dp),
			color = textColor,
			text = title,
		)
	}
}

