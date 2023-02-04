package io.dnajd.presentation.table_task.sheets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Redo
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableTaskAlterDescriptionSheet(
	state: TableTaskScreenState.Success,
	description: String,
	onDescriptionChange: (String) -> Unit,
	onBackClicked: () -> Unit,
) {
	var mutableDescription by remember { mutableStateOf(description) }

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = {
					Text(text = stringResource(R.string.field_edit_description))
				},
				navigationIcon = {
					IconButton(onClick = { onBackClicked() }) {
						Icon(
							modifier = Modifier
								.padding(horizontal = 8.dp),
							imageVector = Icons.Rounded.Close,
							contentDescription = ""
						)
					}
				},
				actions = {
					IconButton(onClick = { /*TODO*/ }) {
						Icon(
							modifier = Modifier
								.padding(horizontal = 8.dp),
							imageVector = Icons.Rounded.Undo,
							contentDescription = ""
						)
					}
					IconButton(onClick = { /*TODO*/ }) {
						Icon(
							modifier = Modifier
								.padding(horizontal = 8.dp),
							imageVector = Icons.Rounded.Redo,
							contentDescription = ""
						)
					}
					TextButton(onClick = { onDescriptionChange(mutableDescription) }) {
						Text(
							fontSize = (15.5).sp,
							text = stringResource(R.string.action_save)
						)
					}
				}
			)
		}
	) { paddingValues ->
		BackHandler { onBackClicked() }
		val focusRequester = remember { FocusRequester() }

		TextField(
			colors = TextFieldDefaults.textFieldColors(
				containerColor = Color.Transparent,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
			),
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight()
				.verticalScroll(rememberScrollState())
				.focusRequester(focusRequester)
				.padding(paddingValues),
			value = mutableDescription,
			onValueChange = { mutableDescription = it },
			textStyle = TextStyle(
				fontSize = (14.5).sp
			),
			placeholder = {
				Text(text = stringResource(R.string.action_tap_to_add_description))
			}
		)

		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
	}
}