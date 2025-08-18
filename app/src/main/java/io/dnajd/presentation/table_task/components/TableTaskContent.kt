package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

	onRenameTaskClicked: (String) -> Unit,
	onChangeTableSheetClicked: () -> Unit,
	onAlterDescriptionSheetClicked: () -> Unit,
	onDeleteTaskClicked: () -> Unit,
) {
	Column(
		modifier = Modifier
			.padding(contentPadding)
			.fillMaxSize()
			.verticalScroll(rememberScrollState()),
	) {
		var expanded by remember { mutableStateOf(false) }

		var taskTitle by remember { mutableStateOf(state.taskCurrent().title) }
		val taskCollected = state.taskCollected()
		LaunchedEffect(taskCollected.title) {
			taskTitle = taskCollected.title
		}

		BugtrackerExpandableTextField(
			modifier = Modifier
				.fillMaxWidth()
				.onFocusChanged { expanded = it.isFocused },
			value = taskTitle,
			onValueChange = { taskTitle = it },
			expanded = expanded,
			textStyle = TextStyle(
				fontSize = 22.sp,
				fontWeight = FontWeight.Thin,
			),
			includeDivider = false,
		) {
			BugtrackerExpandableTextFieldDefaults.Content(
				onCancelClicked = {
					expanded = false
					taskTitle = taskCollected.title
				},
				onConfirmClicked = {
					onRenameTaskClicked(taskTitle)
				},
				confirmEnabled = taskTitle != taskCollected.title && taskTitle.isNotEmpty(),
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
						text = state.parentTableCollected().title,
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

		/* TODO finish this
		TableTaskChildIssuesField(state = state)

		TableTaskAssignedField(state = state)
		 */

		TableTaskIconPairFields(state = state)

		TableTaskActivityContent(state = state)
	}
}
