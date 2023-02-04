package io.dnajd.presentation.project_table_task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.dnajd.bugtracker.ui.project_table_task.TableTaskSheet
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.project_table_task.components.TableTaskContent
import io.dnajd.presentation.project_table_task.dialogs.TableTaskBottomSheetContent

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun TableTaskScreenContent(
    state: TableTaskScreenState.Success,
    bottomDialogState: ModalBottomSheetState,
    onBackClicked: () -> Unit,
    onChangeTableClicked: (Long) -> Unit,
    onChangeTableDialogClicked: () -> Unit,
) {
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
                if(state.dialog is TableTaskSheet.BottomSheet) {
                    TableTaskBottomSheetContent(
                        curTable = state.parentTable,
                        tables = state.dialog.tables,
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
                onChangeTableDialogClicked = onChangeTableDialogClicked,
            )
        }
    }
}
