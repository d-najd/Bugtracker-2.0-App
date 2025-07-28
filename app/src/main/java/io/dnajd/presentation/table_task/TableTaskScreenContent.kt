package io.dnajd.presentation.table_task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState
import io.dnajd.bugtracker.ui.table_task.TableTaskSheet
import io.dnajd.presentation.table_task.components.TableTaskContent
import io.dnajd.presentation.table_task.sheets.TableTaskBottomSheetContent

@OptIn(
	ExperimentalMaterial3Api::class,
)
@Composable
fun TableTaskScreenContent(
	state: TableTaskScreenState.Success,
	sheetState: SheetState,
	onBackClicked: () -> Unit,

	onRenameTaskClicked: (String) -> Unit,
	onChangeTableClicked: (Long) -> Unit,
	onChangeTableSheetClicked: () -> Unit,
	onAlterDescriptionSheetClicked: () -> Unit,
	onBottomSheetDismissed: () -> Unit,
) {
	Scaffold { contentPadding ->
		BackHandler { onBackClicked() }

		TableTaskContent(
			state = state,
			contentPadding = contentPadding,
			onRenameTaskClicked = onRenameTaskClicked,
			onChangeTableSheetClicked = onChangeTableSheetClicked,
			onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
		)
	}

	if (state.sheet is TableTaskSheet.BottomSheet || sheetState.isVisible) {
		ModalBottomSheet(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface.copy(.32f),
			sheetState = sheetState,
			onDismissRequest = {
				onBottomSheetDismissed()
			},
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 1.dp)
					.verticalScroll(rememberScrollState()),
			) {
				TableTaskBottomSheetContent(
					state = state,
					onChangeTableClicked = onChangeTableClicked,
				)
			}
		}
	}
}
