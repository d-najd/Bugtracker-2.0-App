package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnajd.bugtracker.R
import io.dnajd.bugtracker.ui.project_table.ProjectTableEvent
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.table_task.model.TableTask
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
	onCreateTableTaskClicked: (TableTask) -> Unit,
	onTaskClicked: (Long) -> Unit,
	onSwapTablePositionsClicked: (Long, Long) -> Unit,
	onSwitchDropdownMenuClicked: (Long?) -> Unit,
) {
	Card(
		modifier = Modifier
			.width(333.dp)
			.padding(
				start = 8.dp,
				top = 4.dp,
			),
	) {
		val tasks = state
			.tasksCollectedByTableId(table.id)
			.sortedBy { it.position }

		var reorderableList by remember { mutableStateOf(tasks) }        // a list is being stored in case the user moves multiple table items
		val reorderableState = rememberReorderableLazyListState(
			onMove = { from, to ->
				reorderableList = reorderableList
					.toMutableList()
					.apply {
						add(
							to.index,
							removeAt(from.index),
						)
					}
			},
			onDragEnd = { from, to ->
				if (from != to) {
					onMoveTableTasks(
						table.id,
						from,
						to,
					)
				}
			},
		)

		// Tasks are moved using reorderable state, the thing is this component will move the tasks
		// in the ui during the request for moving is sent to the server, because no data will be
		// changed if this request fails we don't have anything to update the tasks so we add an
		// listener instead, if there are more cases consider an alternative system.
		LaunchedEffect(state.events) {
			state.events.collect { event ->
				if (event is ProjectTableEvent.FailedToMoveTableTasks) {
					reorderableList = tasks.sortedBy { it.position }
				}
			}
		}

		LaunchedEffect(tasks) {
			reorderableList = tasks.sortedBy { it.position }
		}

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

		LazyColumn(
			state = reorderableState.listState,
			modifier = Modifier
				.fillMaxWidth()
				.reorderable(reorderableState)
				.heightIn(max = 2000.dp),
		) {
			items(
				reorderableList,
				{ task -> task.id },
			) { task ->
				ReorderableItem(
					reorderableState = reorderableState,
					key = task.id,
				) { isDragging ->
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
				.padding(
					top = 8.dp,
					bottom = 8.dp,
					start = 8.dp,
					end = 8.dp,
				)
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
	val tasks = state.tasksCollectedByTableId(table.id)

	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			modifier = Modifier.padding(start = 8.dp),
			text = table.title,
			fontWeight = FontWeight.SemiBold,
			fontSize = 16.sp,
		)
		Text(
			modifier = Modifier.padding(start = 12.dp),
			text = tasks.size.toString(),
			fontSize = 16.sp,
			color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
		)
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.End,
		) {
			IconButton(
				onClick = {
					onSwitchDropdownMenuClicked(table.id)
				},
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
) {
	val tables = state.tablesCollected()

	Column(
		horizontalAlignment = Alignment.End,
		modifier = Modifier.fillMaxWidth(),
	) {
		DropdownMenu(
			expanded = table.id == state.dropdownOpenedInTableId,
			onDismissRequest = { onSwitchDropdownMenuClicked(null) },
		) {
			DropdownMenuItem(
				text = {
					Text(text = stringResource(R.string.action_rename_column))
				},
				onClick = {
					onTableRename(
						table.id,
						table.title,
					)
					onSwitchDropdownMenuClicked(null)
				},
			)
			if (index != 0) {
				DropdownMenuItem(
					text = {
						Text(text = stringResource(R.string.action_move_column_left))
					},
					onClick = {
						onSwapTablePositionsClicked(
							table.id,
							tables.find { it.position == table.position - 1 }!!.id,
						)
						onSwitchDropdownMenuClicked(null)
					},
				)
			}
			if (index + 1 in tables.indices) {
				DropdownMenuItem(
					text = {
						Text(text = stringResource(R.string.action_move_column_right))
					},
					onClick = {
						onSwapTablePositionsClicked(
							table.id,
							tables.find { it.position == table.position + 1 }!!.id,
						)
						onSwitchDropdownMenuClicked(null)
					},
				)
			}
			DropdownMenuItem(
				text = {
					Text(text = stringResource(R.string.action_delete_table))
				},
				onClick = {
					onDeleteTableClicked(table.id)
					onSwitchDropdownMenuClicked(null)
				},
			)
		}
	}
}

@Composable
private fun ProjectTableCardBottom(
	modifier: Modifier = Modifier,
	state: ProjectTableScreenState.Success,
	table: ProjectTable,

	onCreateTableTaskMenuClicked: (Long?) -> Unit,
	onCreateTableTaskClicked: (TableTask) -> Unit,
) {
	if (table.id == state.taskBeingAddedInTableId) {
		var title by remember { mutableStateOf("") }
		ProjectTableTextFieldCardContent(
			value = title,
			onValueChange = { title = it },
			keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
			keyboardActions = KeyboardActions(
				onDone = {
					onCreateTableTaskClicked(
						TableTask(
							title = title,
							tableId = table.id,
							reporter = "user1",
						),
					)
				},
			),
			isKeyboardEnabled = {
				if (!it) {
					onCreateTableTaskMenuClicked(null)
				}
			},
		)

		Box(modifier = Modifier.height(4.dp))
	} else if (state.taskBeingAddedInTableId == null) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = modifier.clickable { onCreateTableTaskMenuClicked(table.id) },
		) {
			Icon(
				modifier = Modifier.size(32.dp),
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
