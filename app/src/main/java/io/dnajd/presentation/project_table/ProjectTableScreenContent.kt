package io.dnajd.presentation.project_table

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.dnajd.bugtracker.ui.project_table.ProjectTableScreenState
import io.dnajd.bugtracker.ui.util.ProjectTableSelectedTab
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table.components.ProjectTableContent
import io.dnajd.presentation.project_table.components.ProjectTableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectTableScreenContent(
	state: ProjectTableScreenState,
	onBackClicked: () -> Unit,

	onTableRename: (Long, String) -> Unit,
	onMoveTableTasks: (Long, Long) -> Unit,
	onDeleteTableClicked: (Long) -> Unit,
	onCreateTableClicked: () -> Unit,
	onCreateTableTaskMenuClicked: (Long?) -> Unit,
	onCreateTableTaskClicked: (TableTask) -> Unit,
	onTaskClicked: (Long) -> Unit,
	onSwapTablePositionsClicked: (Long, Long) -> Unit,
	onSwitchDropdownMenuClicked: (Long?) -> Unit,
	onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
	/**
	 * If null no filtering, if empty filtering but no string, if string filtering with string
	 */
	var taskFilterString by remember { mutableStateOf<String?>(null) }
	var isTaskDragged by remember { mutableStateOf(false) }

	Scaffold(
		topBar = {
			ProjectTableTopAppBar(
				state = state,
				taskFilterString = taskFilterString,
				isTaskDragged = isTaskDragged,
				onTaskFilterStringChange = { taskFilterString = it },
				onBackClicked = onBackClicked,
				onCreateTableClicked = onCreateTableClicked,
				onSwitchScreenTabClicked = onSwitchScreenTabClicked,
			)
		},
	) { contentPadding ->
		BackHandler { onBackClicked() }

		if (state is ProjectTableScreenState.Loading) {
			LoadingScreen()
			return@Scaffold
		}

		val successState = state as ProjectTableScreenState.Success

		ProjectTableContent(
			state = successState,
			taskFilterString = taskFilterString,
			contentPadding = contentPadding,
			onTaskDraggedStateChange = { isTaskDragged = it },
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
