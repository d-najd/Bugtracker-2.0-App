package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.domain.project_table.model.ProjectTable
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ProjectTableCard(
    projectTable: ProjectTable,
    projectTableLength: Int,
    index: Int,
){
    Card(
        modifier = Modifier
            .width(333.dp)
            .padding(start = 8.dp, top = 4.dp),
    ) {
        ProjectTableCardTop(
            modifier = Modifier.fillMaxWidth(),
            title = projectTable.title,
            taskCount = projectTable.tasks.size,
            index = index,
            projectTableLength = projectTableLength,
        )

        /*
        val state = rememberReorderableLazyListState(onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        })
         */

        var reorderableList by remember { mutableStateOf(projectTable.tasks) }
        val reorderableState = rememberReorderableLazyListState(
            onMove = { from, to ->
                reorderableList = reorderableList.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            },
            onDragEnd = { from, to ->
                reorderableList = projectTable.tasks
                /*
                reorderableList.value = reorderableList.value.toMutableList().apply {
                    add(to, removeAt(from))
                }
                 */
            },
        )

        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .fillMaxWidth()
                .reorderable(reorderableState)
                .heightIn(max = 2000.dp)
        ) {
            items(reorderableList, { task -> task.id }) { task ->
                ReorderableItem(reorderableState = reorderableState, key = task.id) { isDragging ->
                    ProjectTableCardContent(
                        title = task.title,
                        labels = task.labels.map { it.label },
                        id = task.id,
                        reorderableState = reorderableState,
                        isDragging = isDragging,
                    )
                }
            }
        }

        ProjectTableCardBottom(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
        )
    }
}


@Composable
private fun ProjectTableCardTop(
    modifier: Modifier = Modifier,
    title: String,
    taskCount: Int,
    index: Int,
    projectTableLength: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = taskCount.toString(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        )
        var enabled by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            IconButton(
                onClick = {
                    enabled = !enabled
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "",
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DropdownMenu(
                expanded = enabled, onDismissRequest = {
                enabled = false
            }) {
                DropdownMenuItem(text = {
                    Text(text = stringResource(R.string.action_rename_column))
                }, onClick = { /*TODO*/ })
                if(index != 0) {
                    DropdownMenuItem(text = {
                        Text(text = stringResource(R.string.action_move_column_left))
                    }, onClick = { /*TODO*/ })
                }
                if(index+1 < projectTableLength) {
                    DropdownMenuItem(text = {
                        Text(text = stringResource(R.string.action_move_column_right))
                    }, onClick = { /*TODO*/ })
                }
                DropdownMenuItem(text = {
                    Text(text = stringResource(R.string.action_delete_column))
                }, onClick = { /*TODO*/ })
            }
        }
    }
}

@Composable
private fun ProjectTableCardBottom(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    if(enabled) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = Icons.Rounded.Add,
                contentDescription = "",
            )
            Text(
                text = stringResource(R.string.action_create),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth(),
            )
        }
    }
}
