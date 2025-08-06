package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.presentation.components.BugtrackerTwoAppBar
import io.dnajd.presentation.components.BugtrackerTwoAppBarTableBar
import io.dnajd.presentation.util.rememberKeyboardState
import io.dnajd.presentation.util.transparentColors

@Composable
fun ProjectTableTopAppBar(
	state: ProjectTableScreenState,
	taskFilterString: String?,
	isTaskDragged: Boolean,
	onTaskFilterStringChange: (String?) -> Unit,
	onBackClicked: () -> Unit,

	onCreateTableClicked: () -> Unit,
	onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
	val taskFilterFocusRequester = remember { FocusRequester() }
	val isKeyboardOpen by rememberKeyboardState()

	LaunchedEffect(isKeyboardOpen) {
		if (!isKeyboardOpen) {
			onTaskFilterStringChange.invoke(null)
		}
	}

	LaunchedEffect(taskFilterString) {
		if (!isKeyboardOpen && taskFilterString == "") {
			taskFilterFocusRequester.requestFocus()
		}
	}

	BugtrackerTwoAppBar(
		navigationIcon = {
			IconButton(
				onClick = {
					if (taskFilterString == null) {
						onBackClicked()
					} else {
						onTaskFilterStringChange.invoke(null)
					}
				},
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
					contentDescription = "",
				)
			}
		},
		title = {
			if (taskFilterString == null) {
				val title = if (state is ProjectTableScreenState.Success) {
					state.projectCollected().title
				} else {
					stringResource(
						R.string.loading,
					)
				}
				Text(
					text = title,
					fontSize = 20.sp,
				)
			} else {
				val taskNotDraggedColors = TextFieldDefaults.transparentColors()
				val taskDraggedColors = taskNotDraggedColors.copy(
					focusedTextColor = taskNotDraggedColors.disabledTextColor,
					cursorColor = Color.Transparent,
				)

				TextField(
					modifier = Modifier
						.focusRequester(focusRequester = taskFilterFocusRequester)
						.fillMaxWidth()
						.onFocusChanged {
							if (!it.hasFocus && isKeyboardOpen) {
								onTaskFilterStringChange.invoke(null)
							}
						},
					value = taskFilterString,
					onValueChange = {
						if (!isTaskDragged) {
							onTaskFilterStringChange.invoke(it)
						}
					},
					colors = if (!isTaskDragged) taskNotDraggedColors else taskDraggedColors,
					textStyle = TextStyle(
						fontSize = 20.sp,
					),
					singleLine = true,
					placeholder = {
						Text(
							fontSize = 20.sp,
							text = stringResource(R.string.field_search_in_tasks),
						)
					},
				)
			}
		},
		actions = {
			if (taskFilterString == null) {
				IconButton(
					onClick = {
						if (!isKeyboardOpen) {
							onTaskFilterStringChange.invoke("")
						}
					},
				) {
					Icon(
						modifier = Modifier.padding(horizontal = 6.dp),
						imageVector = Icons.Rounded.FilterList,
						contentDescription = "",
					)
				}
				IconButton(onClick = { onCreateTableClicked() }) {
					Icon(
						modifier = Modifier.padding(horizontal = 6.dp),
						imageVector = Icons.Rounded.Add,
						contentDescription = "",
					)
				}
			}
		},
		bottomContent = {
			BugtrackerTwoAppBarTableBar(
				selectedTab = ProjectTableSelectedTab.BOARD,
				onTabClicked = onSwitchScreenTabClicked,
			)
		},
	)
}
