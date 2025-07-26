package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState
import io.dnajd.presentation.components.BugtrackerExpandableTextField
import io.dnajd.presentation.components.BugtrackerExpandableTextFieldDefaults
import io.dnajd.presentation.components.BugtrackerMultipurposeMenu
import io.dnajd.presentation.table_task.components.comment.TableTaskActivityContent

@ExperimentalMaterial3Api
@Composable
fun TableTaskContent(
	state: TableTaskScreenState.Success,
	contentPadding: PaddingValues,

	onChangeTableSheetClicked: () -> Unit,
	onAlterDescriptionSheetClicked: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(contentPadding)
			.padding(
				horizontal = 12.dp,
				vertical = 36.dp,
			),
	) {
		val task = state.task

		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {            /* TODO add functionality for this and the other one in the tables screen, when pressed
				the completed state should revert from false to true and vice versa */
			Checkbox(
				modifier = Modifier.size(16.dp),
				checked = true,
				onCheckedChange = { },
			)
			Text(
				modifier = Modifier
					.padding(start = 12.dp)
					.fillMaxWidth(),
				text = "${stringResource(R.string.field_task).uppercase()}-${task.id}",
				fontSize = 14.sp,
				color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
			)
		}

		var expanded by remember { mutableStateOf(false) }
		var taskTitle = task.title

		BugtrackerExpandableTextField(
			modifier = Modifier
				.padding(top = 13.dp)
				.fillMaxWidth()
				.onFocusChanged { expanded = it.isFocused },
			value = task.title,
			onValueChange = { taskTitle = it },
			expanded = expanded,
			textStyle = TextStyle(
				fontSize = 22.sp,
				fontWeight = FontWeight.Thin,
			),
			includeDivider = false,
		) {
			BugtrackerExpandableTextFieldDefaults.Content(
				onCancelClicked = { expanded = false },
				onConfirmClicked = { },
				confirmEnabled = taskTitle != task.title && taskTitle.isNotEmpty(),
			)
		}

		Card(
			modifier = Modifier
				.padding(top = 18.dp)
				.clickable { onChangeTableSheetClicked() },
			shape = RoundedCornerShape(6.dp),
		) {
			BugtrackerMultipurposeMenu(
				modifier = Modifier.padding(
					top = 8.dp,
					bottom = 8.dp,
					start = 4.dp,
					end = 2.dp,
				),
				text = {
					Text(
						modifier = Modifier.padding(start = 3.5.dp),
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp,
						text = state.parentTable.title,
					)
				},
				includeDropdownArrow = true,
				includeDivider = false,
			)
		}

		TableTaskDescriptionField(
			state = state,
			onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
		)

		TableTaskChildIssuesField(state = state)

		TableTaskAssignedField(state = state)

		TableTaskIconPairFields(state = state)

		TableTaskActivityContent(state = state)

		// TableTaskBasicInfo(state)
	}
}
