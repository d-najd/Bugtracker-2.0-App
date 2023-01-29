package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
    textStyle: TextStyle? = null,
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
            textStyle = textStyle,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            isKeyboardEnabled = isKeyboardEnabled,
            includeDivider = includeDivider,
            dividerColor = if(expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        )

        AnimatedVisibility(
            visible = expanded,
            enter = enter,
            exit = exit,
        ) {
            expandableContent()
        }

        val focusManager = LocalFocusManager.current
        if(!expanded) {
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
                onCancelClicked = { }
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
    ){
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
