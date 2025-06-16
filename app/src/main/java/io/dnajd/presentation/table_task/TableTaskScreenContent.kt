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
	ExperimentalMaterial3Api::class,    // ExperimentalMaterialApi::class
)
@Composable
fun TableTaskScreenContent(
	state: TableTaskScreenState.Success,
	sheetState: SheetState,
	onBackClicked: () -> Unit,
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
			onChangeTableSheetClicked = onChangeTableSheetClicked,
			onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
		)
	}

	if (state.sheet is TableTaskSheet.BottomSheet) {
		ModalBottomSheet(
			containerColor = MaterialTheme.colorScheme.surface,
			contentColor = MaterialTheme.colorScheme.onSurface.copy(.32f),
			onDismissRequest = {
				onBottomSheetDismissed()
			}) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 1.dp)
					.verticalScroll(rememberScrollState()),
			) {
				TableTaskBottomSheetContent(
					curTable = state.parentTable,
					tables = state.sheet.tables,
					onChangeTableClicked = onChangeTableClicked,
				)
			}
		}
	}

	/*
	BottomSheetScaffold(
		sheetContainerColor = MaterialTheme.colorScheme.surface,
		sheetContentColor = MaterialTheme.colorScheme.onSurface.copy(.32f),
		sheetContent = {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.heightIn(min = 1.dp)
					.verticalScroll(rememberScrollState()),
			) {
				if (state.sheet is TableTaskSheet.BottomSheet) {
					TableTaskBottomSheetContent(
						curTable = state.parentTable,
						tables = state.sheet.tables,
						onChangeTableClicked = onChangeTableClicked,
					)
				}
			}
		}
	) {
		Scaffold { contentPadding ->
			BackHandler { onBackClicked() }

			TableTaskContent(
				state = state,
				contentPadding = contentPadding,
				onChangeTableSheetClicked = onChangeTableSheetClicked,
				onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
			)
		}
	}
	 */    /*
	ModalBottomSheetLayout(
		sheetState = bottomDialogState,
		sheetBackgroundColor = MaterialTheme.colorScheme.surface,
		sheetContentColor = MaterialTheme.colorScheme.onSurface.copy(.32f),
		sheetContent = {
			// anchor bs
			Column(
				modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 1.dp)
                    .verticalScroll(rememberScrollState()),
			) {
				if (state.sheet is TableTaskSheet.BottomSheet) {
					TableTaskBottomSheetContent(
						curTable = state.parentTable,
						tables = state.sheet.tables,
						onChangeTableClicked = onChangeTableClicked,
					)
				}
			}
		}
	) {
		Scaffold { contentPadding ->
			BackHandler { onBackClicked() }

			TableTaskContent(
				state = state,
				contentPadding = contentPadding,
				onChangeTableSheetClicked = onChangeTableSheetClicked,
				onAlterDescriptionSheetClicked = onAlterDescriptionSheetClicked,
			)
		}
	}
	 */
}
