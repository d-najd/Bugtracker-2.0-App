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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
		modifier = Modifier.fillMaxSize(), topBar = {
			TopAppBar(title = {
				Text(
					text = stringResource(R.string.field_edit_description), fontSize = 18.sp
				)
			}, navigationIcon = {
				IconButton(onClick = { onBackClicked() }) {
					Icon(
						modifier = Modifier.padding(horizontal = 8.dp),
						imageVector = Icons.Rounded.Close,
						contentDescription = ""
					)
				}
			}, actions = {
				IconButton(onClick = { /*TODO*/ }) {
					Icon(
						imageVector = Icons.AutoMirrored.Rounded.Undo, contentDescription = ""
					)
				}
				IconButton(onClick = { /*TODO*/ }) {
					Icon(
						imageVector = Icons.AutoMirrored.Rounded.Redo, contentDescription = ""
					)
				}
				TextButton(
					enabled = mutableDescription != description,
					onClick = { onDescriptionChange(mutableDescription) }) {
					Text(
						fontSize = 15.sp, text = stringResource(R.string.action_save)
					)
				}
			})
		}) { paddingValues ->
		BackHandler { onBackClicked() }
		val focusRequester = remember { FocusRequester() }

		TextField(
			colors = TextFieldDefaults.colors(
				focusedTextColor = FilledTextFieldTokens.FocusInputColor.value,
				unfocusedTextColor = FilledTextFieldTokens.InputColor.value,
				disabledTextColor = FilledTextFieldTokens.DisabledInputColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
				errorTextColor = FilledTextFieldTokens.ErrorInputColor.value,
				focusedContainerColor = Color.Transparent,
				unfocusedContainerColor = Color.Transparent,
				disabledContainerColor = Color.Transparent,
				errorContainerColor = FilledTextFieldTokens.ContainerColor.value,
				cursorColor = FilledTextFieldTokens.CaretColor.value,
				errorCursorColor = FilledTextFieldTokens.ErrorFocusCaretColor.value,
				selectionColors = LocalTextSelectionColors.current,
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				disabledIndicatorColor = FilledTextFieldTokens.DisabledActiveIndicatorColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledActiveIndicatorOpacity),
				errorIndicatorColor = FilledTextFieldTokens.ErrorActiveIndicatorColor.value,
				focusedLeadingIconColor = FilledTextFieldTokens.FocusLeadingIconColor.value,
				unfocusedLeadingIconColor = FilledTextFieldTokens.LeadingIconColor.value,
				disabledLeadingIconColor = FilledTextFieldTokens.DisabledLeadingIconColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledLeadingIconOpacity),
				errorLeadingIconColor = FilledTextFieldTokens.ErrorLeadingIconColor.value,
				focusedTrailingIconColor = FilledTextFieldTokens.FocusTrailingIconColor.value,
				unfocusedTrailingIconColor = FilledTextFieldTokens.TrailingIconColor.value,
				disabledTrailingIconColor = FilledTextFieldTokens.DisabledTrailingIconColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledTrailingIconOpacity),
				errorTrailingIconColor = FilledTextFieldTokens.ErrorTrailingIconColor.value,
				focusedLabelColor = FilledTextFieldTokens.FocusLabelColor.value,
				unfocusedLabelColor = FilledTextFieldTokens.LabelColor.value,
				disabledLabelColor = FilledTextFieldTokens.DisabledLabelColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledLabelOpacity),
				errorLabelColor = FilledTextFieldTokens.ErrorLabelColor.value,
				focusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
				unfocusedPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
				disabledPlaceholderColor = FilledTextFieldTokens.DisabledInputColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
				errorPlaceholderColor = FilledTextFieldTokens.InputPlaceholderColor.value,
				focusedSupportingTextColor = FilledTextFieldTokens.FocusSupportingColor.value,
				unfocusedSupportingTextColor = FilledTextFieldTokens.SupportingColor.value,
				disabledSupportingTextColor = FilledTextFieldTokens.DisabledSupportingColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledSupportingOpacity),
				errorSupportingTextColor = FilledTextFieldTokens.ErrorSupportingColor.value,
				focusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
				unfocusedPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
				disabledPrefixColor = FilledTextFieldTokens.InputPrefixColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
				errorPrefixColor = FilledTextFieldTokens.InputPrefixColor.value,
				focusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
				unfocusedSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
				disabledSuffixColor = FilledTextFieldTokens.InputSuffixColor.value
				    .copy(alpha = FilledTextFieldTokens.DisabledInputOpacity),
				errorSuffixColor = FilledTextFieldTokens.InputSuffixColor.value,
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
			})

		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
	}
}