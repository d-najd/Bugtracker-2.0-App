package io.dnajd.presentation.project_table

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
	onMoveTableTasks: (Long, Int, Int) -> Unit,
	onDeleteTableClicked: (Long) -> Unit,
	onCreateTableClicked: () -> Unit,
	onCreateTableTaskMenuClicked: (Long?) -> Unit,
	onCreateTableTaskClicked: (TableTask) -> Unit,
	onTaskClicked: (Long) -> Unit,
	onSwapTablePositionsClicked: (Long, Long) -> Unit,
	onSwitchDropdownMenuClicked: (Long?) -> Unit,
	onSwitchScreenTabClicked: (ProjectTableSelectedTab) -> Unit,
) {
	Scaffold(
		topBar = {
			ProjectTableTopAppBar(
				state = state,
				onBackClicked = onBackClicked,
				onCreateTableClicked = onCreateTableClicked,
				onSwitchScreenTabClicked = onSwitchScreenTabClicked,
			)
		}
	) { contentPadding ->
		BackHandler { onBackClicked() }

		if (state is ProjectTableScreenState.Loading) {
			LoadingScreen()
			return@Scaffold
		}

		val successState = state as ProjectTableScreenState.Success

		ProjectTableContent(
			state = successState,
			contentPadding = contentPadding,
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