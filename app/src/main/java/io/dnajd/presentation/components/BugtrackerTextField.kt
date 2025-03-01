package io.dnajd.presentation.components

import android.annotation.SuppressLint
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.dnajd.bugtracker.R

// TODO document this and make it easy to use and get rid of the underline
@Composable
fun BugtrackerTextField(
	@SuppressLint("ModifierParameter") modifierText: Modifier = Modifier,
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit,
	label: String? = null,
	includeDivider: Boolean = true,
	textStyle: TextStyle = TextStyle(fontSize = 15.sp),
	textColor: Color = MaterialTheme.colorScheme.onSurface,
	dividerColor: Color = MaterialTheme.colorScheme.onSurface,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	isKeyboardEnabled: ((Boolean) -> Unit)? = null,
) {
	Column(
		modifier = modifier,
	) {
		if (label != null) {
			Text(text = label)
		}
		BasicTextField(
			modifier = modifierText.padding(top = 8.dp, bottom = 3.dp),
			value = value,
			onValueChange = onValueChange,
			textStyle = textStyle.copy(
				color = textColor
			),
			singleLine = true,
			keyboardOptions = keyboardOptions,
			keyboardActions = keyboardActions,
		)

		if (isKeyboardEnabled != null) {
			val view = LocalView.current
			DisposableEffect(view) {
				val listener = ViewTreeObserver.OnGlobalLayoutListener {
					val isKeyboardEnabledTemp = ViewCompat.getRootWindowInsets(view)
						?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
					isKeyboardEnabled(isKeyboardEnabledTemp)
				}
				view.viewTreeObserver.addOnGlobalLayoutListener(listener)
				onDispose {
					view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
				}
			}
		}

		if (includeDivider) {
			Divider(color = dividerColor)
		}
	}
}

@Preview
@Composable
fun BugtrackerTextFieldPreview() {
	var title by remember { mutableStateOf("") }
	BugtrackerCard {
		BugtrackerTextField(
			modifierText = Modifier
				.fillMaxWidth(),
			label = stringResource(R.string.field_project_title),
			value = title,
			onValueChange = { title = it }
		)
	}
}