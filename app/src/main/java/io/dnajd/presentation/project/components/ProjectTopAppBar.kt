package io.dnajd.presentation.project.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.presentation.util.rememberKeyboardState
import io.dnajd.presentation.util.transparentColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTopAppBar(
	projectFilterString: String?,
	onProjectFilterStringChange: (String?) -> Unit,

	onCreateProjectClicked: () -> Unit,
) {
	val projectFilterFocusRequester = remember { FocusRequester() }
	val isKeyboardOpen by rememberKeyboardState()

	LaunchedEffect(isKeyboardOpen) {
		if (!isKeyboardOpen) {
			onProjectFilterStringChange.invoke(null)
		}
	}

	LaunchedEffect(projectFilterString) {
		if (!isKeyboardOpen && projectFilterString == "") {
			projectFilterFocusRequester.requestFocus()
		}
	}

	TopAppBar(
		navigationIcon = {
			if (projectFilterString != null) {
				IconButton(
					onClick = { onProjectFilterStringChange.invoke(null) },
				) {
					Icon(
						imageVector = Icons.AutoMirrored.Filled.ArrowBack,
						contentDescription = null,
					)
				}
			}
		},
		title = {
			if (projectFilterString == null) {
				Text(
					text = stringResource(R.string.field_projects),
				)
			} else {
				TextField(
					modifier = Modifier
						.fillMaxWidth()
						.focusRequester(projectFilterFocusRequester),
					value = projectFilterString,
					onValueChange = { onProjectFilterStringChange.invoke(it) },
					colors = TextFieldDefaults.transparentColors(),
					textStyle = TextStyle(
						fontSize = 20.sp,
					),
					singleLine = true,
					placeholder = {
						Text(
							fontSize = 20.sp,
							text = stringResource(R.string.field_search_projects),
						)
					},
				)
			}
		},
		actions = {
			if (projectFilterString == null) {
				IconButton(
					onClick = {
						onProjectFilterStringChange.invoke("")
					},
				) {
					Icon(
						modifier = Modifier.padding(horizontal = 6.dp),
						imageVector = Icons.Rounded.Search,
						contentDescription = "",
					)
				}
				IconButton(onClick = { onCreateProjectClicked() }) {
					Icon(
						modifier = Modifier.padding(horizontal = 6.dp),
						imageVector = Icons.Rounded.Add,
						contentDescription = "",
					)
				}
			}
		},
	)
}
