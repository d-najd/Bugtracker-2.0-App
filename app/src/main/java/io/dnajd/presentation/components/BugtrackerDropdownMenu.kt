
package io.dnajd.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * extension of [DropdownMenu] with the style of [BugtrackerMultipurposeMenu]
 *
 * @param modifier modifier for the [BugtrackerMultipurposeMenu]
 * @param dropdownModifier for the dropdown menu
 * @param title optional title located on top of [text]
 * @param text the text of the dropdown menu
 * @param expanded whether the menu is expanded or not
 * possible to change the text with the text of the selected item or some other text
 * @param includeDropdownArrow if enabled an dropdown arrow will be put at the end, this arrow will
 * spin up and point upwards when the menu is expanded, on by default
 * @param offset [DpOffset] to be added to the position of the menu
 * @param onDismissRequest called when the user requests to dismiss the menu, such as by tapping
 * outside the menu's bounds
 * @param onClick gets triggered when [text] is clicked
 * @param dropdownContent content of the dropdown menu,
 * @see [BugtrackerDropdownMenu]
 * @see [DropdownMenu]
 * @sample BugtrackerDropdownMenuPreview()
 *
 * TODO animate the arrow
 */
@Composable
fun BugtrackerDropdownMenu(
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    title: String? = null,
    text: String,
    expanded: Boolean,
    includeDropdownArrow: Boolean = true,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
    dropdownContent: @Composable ColumnScope.() -> Unit,
) {
    Column {
        BugtrackerMultipurposeMenu(
            modifier = modifier,
            text = text,
            title = title,
            includeDropdownArrow = includeDropdownArrow,
            onClick = onClick,
        )

        DropdownMenu(
            expanded = expanded,
            modifier = dropdownModifier
                .background(MaterialTheme.colorScheme.primaryContainer),
            onDismissRequest = onDismissRequest,
            offset = offset,
        ) {
            dropdownContent()
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 175,
)
@Composable
private fun BugtrackerDropdownMenuPreview(){
    var expanded by remember { mutableStateOf(false) }

    BugtrackerCard(title = "Example") {
        BugtrackerDropdownMenu(
            onClick = { expanded = !expanded },
            expanded = expanded,
            title = "Top Title",
            text = "Selected Item",
            onDismissRequest = {
                expanded = false
            },
        ) {
            DropdownMenuItem(text = { Text(text = "Dropdown Item 1") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text(text = "Dropdown Item 2") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text(text = "Dropdown Item 3") }, onClick = { expanded = false })
        }
    }
}
