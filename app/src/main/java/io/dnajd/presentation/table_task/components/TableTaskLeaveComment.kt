package io.dnajd.presentation.table_task.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.R
import io.dnajd.presentation.util.rememberKeyboardState

@Composable
fun TableTaskLeaveComment() {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Bottom,
	) {
		HorizontalDivider()

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.surfaceContainer),
		) {
			val isKeyboardOpen by rememberKeyboardState()
			val focusRequester = remember { FocusRequester() }
			val focusManager = LocalFocusManager.current
			var isFocused by remember { mutableStateOf(false) }

			LaunchedEffect(isKeyboardOpen, isFocused) {
				if (!isKeyboardOpen && isFocused) {
					focusManager.clearFocus(true)
				}
			}

			TextField(
				modifier = Modifier
					.weight(1f)
					.focusRequester(focusRequester)
					.onFocusChanged { focusState ->
						isFocused = focusState.isFocused
					},
				value = "Test",
				onValueChange = { },
				placeholder = {
					Text(stringResource(R.string.info_comment_first))
				},
				colors = TextFieldDefaults.colors(
					focusedContainerColor = Color.Transparent,
					unfocusedContainerColor = Color.Transparent,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
				),
				keyboardOptions = KeyboardOptions.Default.copy(
					imeAction = ImeAction.Send,
				),
				keyboardActions = KeyboardActions(
					onSend = {

					},
				),
			)

			IconButton(
				modifier = Modifier
					.padding(end = 8.dp),
				onClick = {

				},
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.Send,
					contentDescription = null,
				)
			}
		}
	}
}
