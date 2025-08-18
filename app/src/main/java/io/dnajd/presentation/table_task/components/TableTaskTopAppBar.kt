package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableTaskTopAppBar(
	state: TableTaskScreenState.Success,

	onDeleteTaskClicked: () -> Unit,
) {
	TopAppBar(
		windowInsets = WindowInsets(
			top = 0.dp,
			bottom = 0.dp,
		),
		navigationIcon = {
			Checkbox(
				modifier = Modifier.size(16.dp),
				checked = true,
				enabled = false,
				colors = CheckboxDefaults.colors()
					.copy(
						disabledCheckedBoxColor = CheckboxDefaults.colors().checkedBoxColor,
						disabledBorderColor = CheckboxDefaults.colors().checkedBorderColor,
					),
				onCheckedChange = { },
			)
		},
		title = {
			Text(
				modifier = Modifier
					.padding(start = 12.dp),
				text = "${stringResource(R.string.field_task).uppercase()}-${state.taskCollected().id}",
				fontSize = 14.sp,
				color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
			)
		},
		actions = {
			var dropdownExpanded by remember { mutableStateOf(false) }

			IconButton(
				onClick = {
					dropdownExpanded = !dropdownExpanded
				},
			) {
				Icon(
					imageVector = Icons.Default.MoreVert,
					contentDescription = null,
				)
			}

			DropdownMenu(
				expanded = dropdownExpanded,
				onDismissRequest = {
					dropdownExpanded = false
				},
			) {
				DropdownMenuItem(
					text = { Text(stringResource(R.string.action_remove_table_task)) },
					onClick = {
						onDeleteTaskClicked.invoke()
					},
				)
			}

		},
	)
}
