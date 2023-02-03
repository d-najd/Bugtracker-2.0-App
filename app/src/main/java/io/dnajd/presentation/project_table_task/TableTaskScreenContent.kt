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
import io.dnajd.presentation.components.LoadingScreen
import io.dnajd.presentation.project_table_task.components.TableTaskContent
import io.dnajd.presentation.project_table_task.dialogs.TableTaskBottomSheetContent

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun TableTaskScreenContent(
    state: TableTaskScreenState,
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
                val successState = state as TableTaskScreenState.Success
                if(successState.dialog is TableTaskSheet.BottomSheet) {
                    TableTaskBottomSheetContent(
                        curTable = successState.parentTable,
                        tables = successState.dialog.tables,
                        onChangeTableClicked = onChangeTableClicked,
                    )
                }
            }
        }
    ) {
        Scaffold { contentPadding ->
            BackHandler { onBackClicked() }

            if(state is TableTaskScreenState.Success) {
                TableTaskContent(
                    state = state,
                    contentPadding = contentPadding,
                    onChangeTableDialogClicked = onChangeTableDialogClicked,
                )
            } else if(state is TableTaskScreenState.AlterTaskDescription) {
                TableTaskAlterDescriptionContent(
                    state = state,
                )
            }
        }
    }
}
