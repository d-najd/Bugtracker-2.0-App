package io.dnajd.presentation.project_table_task.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.project_table_task.components.TableTaskExpandableMenu
import io.dnajd.presentation.project_table_task.components.TableTaskIconPairField

@Composable
fun TableTaskChildIssuesField(
    state: TableTaskScreenState.Success,
) {
    var expanded by remember { mutableStateOf(false) }
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
                        text = state.task.childTasks.size.toString(),
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
            for (childTask in state.task.childTasks) {
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
                                text = "${stringResource(R.string.label_task).uppercase()}-${childTask.id}",
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