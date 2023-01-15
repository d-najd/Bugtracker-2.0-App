package io.dnajd.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R

/**
 * Multipurpose button which has title on the top, text which is clickable, an arrow at the far right
 * and a divider on the bottom
 *
 * @param modifier modifier for the entire composable
 * @param title optional title located on top of [text]
 * @param text the text of the menu
 * @param includeDropdownArrow if enabled an dropdown arrow will be put at the end, this arrow will
 * point downwards, off by default
 * @param includeDivider if enabled will include divider under the text, on by default
 * @param onClick gets triggered when [text] is clicked
 * @sample BugtrackerMultipurposeMenuPreview()
 */
@Composable
fun BugtrackerMultipurposeMenu(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String,
    includeDropdownArrow: Boolean = false,
    includeDivider: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                modifier = Modifier
                    .padding(start = MULTIPURPOSE_MENU_TEXT_START_PADDING),
                text = title,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 5.dp).let {
                if(onClick != null){
                    it.clickable {
                        onClick()
                    }
                }
                it
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = MULTIPURPOSE_MENU_TEXT_START_PADDING + 1.dp
                    ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                text = text,
            )

            if (includeDropdownArrow) {
                Box(
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = ""
                    )
                }
            }
        }

        if(includeDivider) {
            Divider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

/** start padding for the text so the text isn't right at the beginning */
private val MULTIPURPOSE_MENU_TEXT_START_PADDING = 2.5.dp


@Preview(
    widthDp = 300,
    heightDp = 175,
)
@Composable
private fun BugtrackerMultipurposeMenuPreview(){
    var displayDialog by remember { mutableStateOf(false) }

    BugtrackerCard(title = "Example") {
        BugtrackerMultipurposeMenu(
            text = "Show Dialog",
            includeDropdownArrow = false,
            onClick = {
                displayDialog = !displayDialog
            },
        )
    }

    if(displayDialog) {
        AlertDialog(
            text = {
                Text(text = "hello")
            },
            confirmButton = {
                TextButton(onClick = { displayDialog = false }) {
                    Text(text = stringResource(R.string.action_confirm))
                }
            },
            onDismissRequest = { displayDialog = false },
        )
    }
}

