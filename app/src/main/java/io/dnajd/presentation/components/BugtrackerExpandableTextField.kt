package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import io.dnajd.bugtracker.R

// TODO FINISH THIS
@Composable
fun BugtrackerExpandableTextField(
	@SuppressLint("ModifierParameter") modifierText: Modifier = Modifier,
	modifier: Modifier = Modifier,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	isKeyboardEnabled: ((Boolean) -> Unit)? = null,
	textStyle: TextStyle = TextStyle.Default,
	textColor: Color = MaterialTheme.colorScheme.onSurface,
	includeDivider: Boolean = true,
	value: String,
	onValueChange: (String) -> Unit,
	label: String? = null,
	expanded: Boolean,
	enter: EnterTransition = fadeIn() + expandVertically(),
	exit: ExitTransition = fadeOut() + shrinkVertically(),
	expandableContent: @Composable () -> Unit,
) {
	Column(
		modifier = modifier,
	) {
		BugtrackerTextField(
			modifierText = modifierText,
			value = value,
			onValueChange = onValueChange,
			label = label,
			textStyle = textStyle.copy(
				color = textColor,
			),
			keyboardActions = keyboardActions,
			keyboardOptions = keyboardOptions,
			isKeyboardEnabled = isKeyboardEnabled,
			includeDivider = includeDivider,
			dividerColor = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
		)

		AnimatedVisibility(
			visible = expanded,
			enter = enter,
			exit = exit,
		) {
			expandableContent()
		}

		val focusManager = LocalFocusManager.current
		if (!expanded) {
			focusManager.clearFocus()
		}
	}
}

@Preview
@Composable
private fun BugtrackerExpandableTextFieldPreview() {
	val originalTitle = "Title"
	var title by remember { mutableStateOf(originalTitle) }
	var expanded by remember { mutableStateOf(false) }

	BugtrackerCard {
		BugtrackerExpandableTextField(
			modifierText = Modifier
				.fillMaxWidth()
				.onFocusChanged { expanded = it.isFocused },
			expanded = expanded,
			value = title,
			onValueChange = { title = it },
			label = stringResource(R.string.field_project),
		) {
			BugtrackerExpandableTextFieldDefaults.Content(
				confirmEnabled = (title != originalTitle) && title.isNotBlank(),
				onConfirmClicked = { },
				onCancelClicked = { },
			)
		}
	}
}

object BugtrackerExpandableTextFieldDefaults {
	@Composable
	fun Content(
		cancelText: String = stringResource(R.string.action_cancel),
		confirmText: String = stringResource(R.string.action_save),
		confirmEnabled: Boolean = true,
		onCancelClicked: () -> Unit,
		onConfirmClicked: () -> Unit,
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.End,
		) {
			TextButton(onClick = onCancelClicked) {
				Text(
					text = cancelText,
				)
			}
			TextButton(
				enabled = confirmEnabled,
				onClick = onConfirmClicked,
			) {
				Text(text = confirmText)
			}
		}
	}
}
