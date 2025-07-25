package io.dnajd.presentation.project_table.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.domain.table_task.model.TableTask

@Composable
fun ProjectTableContent(
	state: ProjectTableScreenState.Success,
	contentPadding: PaddingValues,

	onTableRename: (Long, String) -> Unit,
	onMoveTableTasks: (Long, Int, Int) -> Unit,
	onDeleteTableClicked: (Long) -> Unit,
	onCreateTableTaskMenuClicked: (Long?) -> Unit,
	onCreateTableTaskClicked: (TableTask) -> Unit,
	onTaskClicked: (Long) -> Unit,
	onSwapTablePositionsClicked: (Long, Long) -> Unit,
	onSwitchDropdownMenuClicked: (Long?) -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.horizontalScroll(rememberScrollState())
			.verticalScroll(rememberScrollState())
			.padding(contentPadding)
			.padding(5.dp),
	) {
		val tables = state.tables()
		for ((index, table) in tables
			.sortedBy { it.position }
			.withIndex()) {
			ProjectTableCard(
				state = state,
				table = table,
				index = index,
				onTableRename = onTableRename,
				onMoveTableTasks = onMoveTableTasks,
				onDeleteTableClicked = onDeleteTableClicked,
				onCreateTableTaskMenuClicked = onCreateTableTaskMenuClicked,
				onCreateTableTaskClicked = onCreateTableTaskClicked,
				onTaskClicked = onTaskClicked,
				onSwapTablePositionsClicked = onSwapTablePositionsClicked,
				onSwitchDropdownMenuClicked = onSwitchDropdownMenuClicked,
			)
		}
	}
}
