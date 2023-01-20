package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R

// TODO document this and make it easy to use
@Composable
fun BugtrackerTextField(
    @SuppressLint("ModifierParameter") modifierText: Modifier = Modifier,
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    label: String? = null,
    includeDivider: Boolean = true,
) {
    Column(
        modifier = modifier,
    ) {
        if(label != null) {
            Text(text = label)
        }

        BasicTextField(
            modifier = modifierText.padding(top = 8.dp, bottom = 3.dp),
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(
                fontSize = 15.sp,
            ),
            visualTransformation = VisualTransformation.None,
            singleLine = true
        )

        if(includeDivider) {
            Divider(color = MaterialTheme.colorScheme.onSurface)
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
            title = title,
            onTitleChange = { title = it }
        )
    }
}