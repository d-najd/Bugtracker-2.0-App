package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.clickable
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
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun ProjectTableCard(
    state: ProjectTableScreenState.Success,
    table: ProjectTable,
    index: Int,

    onTableRename: (Long, String) -> Unit,
    onMoveTableTasks: (Long, Int, Int) -> Unit,
    onDeleteTableClicked: (Long) -> Unit,
    onCreateTableTaskMenuClicked: (Long?) -> Unit,
    onCreateTableTaskClicked: (ProjectTableTask) -> Unit,
    onTaskClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Long?) -> Unit,
){
    Card(
        modifier = Modifier
            .width(333.dp)
            .padding(start = 8.dp, top = 4.dp),
    ) {
        ProjectTableCardTop(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            table = table,
            index = index,
            onTableRename = onTableRename,
            onDeleteTableClicked = onDeleteTableClicked,
            onSwapTablePositionsClicked = onSwapTablePositionsClicked,
            onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
        )

        var reorderableList by remember { mutableStateOf(table.tasks) }
        // a list is being stored in case the user moves multiple table items
        val reorderableState = rememberReorderableLazyListState(
            onMove = { from, to ->
                reorderableList = reorderableList.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
            },
            onDragEnd = { from, to ->
                if(from != to) {
                    onMoveTableTasks(
                        table.id,
                        from,
                        to,
                    )
                }
            },
        )

        // refresh when project tables get altered
        LaunchedEffect(state.tables){
            reorderableList = table.tasks.sortedBy { it.position }
        }

        /*
            refresh when an task gets moved, for some reason moving tasks does not count as state
            change and events are not reliable so I am stuck with this
         */
        LaunchedEffect(state.manualTableTasksRefresh) {
            reorderableList = table.tasks.sortedBy { it.position }
        }

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
                        value = task.title,
                        labels = task.labels.map { it.label },
                        taskId = task.id,
                        reorderableState = reorderableState,
                        isDragging = isDragging,
                        onTaskClicked = onTaskClicked,
                    )
                }
            }
        }

        ProjectTableCardBottom(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            state = state,
            table = table,
            onCreateTableTaskMenuClicked = onCreateTableTaskMenuClicked,
            onCreateTableTaskClicked = onCreateTableTaskClicked,
        )
    }
}

@Composable
private fun ProjectTableCardTop(
    modifier: Modifier = Modifier,
    state: ProjectTableScreenState.Success,
    table: ProjectTable,
    index: Int,

    onTableRename: (Long, String) -> Unit,
    onDeleteTableClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Long?) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = table.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = table.tasks.size.toString(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            IconButton(
                onClick = {
                    onSwitchDropdownMenuClicked(table.id)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "",
                )
            }
        }
        
        ProjectTableDropdownMenu(
            state = state,
            table = table,
            index = index,
            onTableRename = onTableRename,
            onDeleteTableClicked = onDeleteTableClicked,
            onSwapTablePositionsClicked = onSwapTablePositionsClicked,
            onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
        )
    }
}

@Composable
private fun ProjectTableDropdownMenu(
    state: ProjectTableScreenState.Success,
    table: ProjectTable,
    index: Int,

    onTableRename: (Long, String) -> Unit,
    onDeleteTableClicked: (Long) -> Unit,
    onSwapTablePositionsClicked: (Long, Long) -> Unit,
    onSwitchDropdownMenuClicked: (Long?) -> Unit,
){
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DropdownMenu(
            expanded = table.id == state.dropdownDialogSelectedTableId,
            onDismissRequest = { onSwitchDropdownMenuClicked(null) }
        ) {
            DropdownMenuItem(text = {
                Text(text = stringResource(R.string.action_rename_column))
            }, onClick = {
                onTableRename(table.id, table.title)
                onSwitchDropdownMenuClicked(null)
            })
            if(index != 0) {
                DropdownMenuItem(text = {
                    Text(text = stringResource(R.string.action_move_column_left))
                }, onClick = {
                    onSwapTablePositionsClicked(table.id, state.tables.find { it.position == table.position - 1 }!!.id)
                    onSwitchDropdownMenuClicked(null)
                })
            }
            if(index + 1 in state.tables.indices) {
                DropdownMenuItem(text = {
                    Text(text = stringResource(R.string.action_move_column_right))
                }, onClick = {
                    onSwapTablePositionsClicked(table.id, state.tables.find { it.position == table.position + 1 }!!.id)
                    onSwitchDropdownMenuClicked(null)
                })
            }
            DropdownMenuItem(text = {
                Text(text = stringResource(R.string.action_delete_table))
            }, onClick = {
                onDeleteTableClicked(table.id)
                onSwitchDropdownMenuClicked(null)
            })
        }
    }
}

@Composable
private fun ProjectTableCardBottom(
    modifier: Modifier = Modifier,
    state: ProjectTableScreenState.Success,
    table: ProjectTable,

    onCreateTableTaskMenuClicked: (Long?) -> Unit,
    onCreateTableTaskClicked: (ProjectTableTask) -> Unit,
) {
    if(table.id == state.createTableItemSelectedTableId) {
        var title by remember { mutableStateOf("") }
        ProjectTableTextFieldCardContent(
            value = title,
            onValueChange = {
                if(it.isNotBlank() && it.last() == '\n'){
                    onCreateTableTaskClicked(
                        ProjectTableTask(
                            title = title,
                            tableId = table.id,
                            reporter = "user1",
                        )
                    )
                } else {
                    title = it
                } },
            onKeyboardStateChange = {
                if(!it) {
                   onCreateTableTaskMenuClicked(null)
                }
            }
        )
        Box(modifier = Modifier.height(4.dp))
    } else if (state.createTableItemSelectedTableId == null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable { onCreateTableTaskMenuClicked(table.id) },
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
    } else {
        Box(modifier = Modifier.height(4.dp))
    }
}
