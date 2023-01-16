package io.dnajd.presentation.project_table_task.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.dnajd.presentation.components.BugtrackerMultipurposeMenu

/**
 * @param modifier modifier for the entire composable
 * @param expanded if true the content will be expanded and vice versa
 * @param enter EnterTransition(s) used for the appearing animation, fading in while expanding
 *              vertically by default
 * @param exit ExitTransition(s) used for the disappearing animation, fading out while
 *             shrinking vertically by default
 * @param onClick optional clickable that gets triggered when the menu gets clicked, optional
 *                because the entire composable could be made clickable using [modifier]
 * @param menuContent menu that functions similarly to dropdown menu
 * @param expandableContent the content that will get expanded when [expanded] is true
 * @sample TableTaskExpandableMenuPreview()
 */
@Composable
fun TableTaskExpandableMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    enter: EnterTransition = fadeIn() + expandVertically(),
    exit: ExitTransition = fadeOut() + shrinkVertically(),
    onClick: (() -> Unit)? = null,
    menuContent: @Composable RowScope.() -> Unit,
    expandableContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        BugtrackerMultipurposeMenu(
            dividerThickness = 4.dp,
            onClick = onClick,
        ) {
            menuContent()
        }

        AnimatedVisibility(
            visible = expanded,
            enter = enter,
            exit = exit,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                expandableContent()
            }
        }

        Divider(
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

@Preview
@Composable
fun TableTaskExpandableMenuPreview() {

}