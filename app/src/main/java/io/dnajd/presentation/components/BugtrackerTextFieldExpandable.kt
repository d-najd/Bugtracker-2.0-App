package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.dnajd.bugtracker.R

// TODO FINISH THIS
@Composable
fun BugtrackerTextFieldExpandable(
    @SuppressLint("ModifierParameter") modifierText: Modifier = Modifier,
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    label: String? = null,
    enter: EnterTransition = fadeIn() + expandVertically(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    expandableContent: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        BugtrackerTextField(
            modifierText = modifierText.onFocusChanged { expanded = it.isFocused },
            title = title,
            onTitleChange = onTitleChange,
            label = label,
        )

        AnimatedVisibility(
            visible = expanded,
            enter = enter,
            exit = exit,
        ) {
            expandableContent()
        }
    }
}

@Preview
@Composable
fun BugtrackerTextFieldExpandable() {
    var title by remember { mutableStateOf("") }

    BugtrackerCard {
        BugtrackerTextFieldExpandable(
            modifierText = Modifier.fillMaxWidth(),
            title = title,
            onTitleChange = { title = it },
            label = stringResource(R.string.field_project),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.action_save))
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.action_cancel))
                }
            }
        }
    }
}