package io.dnajd.presentation.project_table_task.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.presentation.components.BugtrackerCard

/**
 * @param modifier modifier for the composable
 * @param title title located on top of [text]
 * @param text optional text located right of the [content]
 * @param onClick optional clickable which covers the whole composable
 * @param content "icon content", this is optional content located to the left of [text]
 * @sample TableTaskIconPairFieldPreview()
 */
@Composable
fun TableTaskIconPairField(
    modifier: Modifier = Modifier,
    title: String,
    text: String? = null,
    onClick: (() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    Column(
        modifier = if(onClick != null) modifier.clickable(onClick = onClick) else modifier,
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        )

        Row(
            modifier = Modifier.padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content?.let { content() }
            if(text != null) {
                val textPadding = if (content == null) 0.dp else 6.dp
                Text(
                    modifier = Modifier.padding(start = textPadding),
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin,
                )
            }
        }
    }
}

@Preview
@Composable
fun TableTaskIconPairFieldPreview() {
    BugtrackerCard {
        TableTaskIconPairField(
            modifier = Modifier.fillMaxWidth(),
            title = "Reporter",
            text = "user1",
            content = {
                Box(
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
                }
            },
            onClick = { /*TODO*/ }
        )
    }
}