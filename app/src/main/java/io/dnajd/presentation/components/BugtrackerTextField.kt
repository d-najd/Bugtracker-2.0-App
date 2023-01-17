package io.dnajd.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

// TODO document this and make it easy to use
@Composable
fun BugtrackerTextField(
    @SuppressLint("ModifierParameter") modifierText: Modifier = Modifier,
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    label: String? = null,
) {
    Column(
        modifier = modifier,
    ) {
        if(label != null) {
            Text(text = label)
        }

        BasicTextField(
            modifier = modifierText,
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(
                fontSize = 15.sp,
            ),
            visualTransformation = VisualTransformation.None,
            singleLine = true
        )

        Divider(color = MaterialTheme.colorScheme.onSurface)
    }

}