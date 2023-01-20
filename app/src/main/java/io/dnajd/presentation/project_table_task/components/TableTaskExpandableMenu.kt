package io.dnajd.presentation.project_table_task.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table_task.model.ProjectTableChildTask
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
    var expanded by remember { mutableStateOf(false) }
    val childTasks = listOf(
        ProjectTableChildTask(
            id = 1,
            title = "Title 1",
            tableId = 1,
        ),
        ProjectTableChildTask(
            id = 2,
            title = "Title 2",
            tableId = 2,
        )
    )

    TableTaskExpandableMenu(
        onClick = { expanded = !expanded },
        expanded = expanded,
        menuContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(start = 3.5.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = stringResource(R.string.field_child_issues),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = childTasks.size.toString(),
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    )

                    Icon(
                        modifier = Modifier.padding(start = 3.dp, end = 4.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = ""
                    )
                }
            }
        },
        expandableContent = {
            for (childTask in childTasks) {
                TableTaskIconPairField(
                    modifier = Modifier.padding(top = 6.dp),
                    iconContent = {
                        Icon(
                            imageVector = Icons.Default.TaskAlt,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = ""
                        )
                    },
                    textContent = {
                        Text(text = childTask.title)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${stringResource(R.string.field_task).uppercase()}-${childTask.id}",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            )
                            Text(
                                text = " = ",
                                color = colorResource(R.color.coral),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 26.sp,
                            )
                            Text(
                                text = "INSERT TABLE NAME",
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                            )
                        }
                    }
                )
            }
        }
    )
}