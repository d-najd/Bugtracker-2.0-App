package io.dnajd.presentation.project_table_task

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.dnajd.bugtracker.ui.project_table_task.TableTaskDialog
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.bugtracker.ui.project_user_management.ProjectUserManagementDialog
import io.dnajd.presentation.project_table_task.components.TableTaskContent
import io.dnajd.presentation.project_user_management.dialogs.AddUserToProjectDialog
import io.dnajd.presentation.project_user_management.dialogs.ConfirmLastAuthorityRemovalDialog
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun TableTaskScreenContent(
    state: TableTaskScreenState.Success,
    bottomDialogState: ModalBottomSheetState,
    onBackClicked: () -> Unit,
    onChangeTableClicked: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = bottomDialogState,
        sheetContent = {
            Text(text = "He")
            Text(text = "Hello2")
            Text(text = "Hello3")
            Text(text = "Hello4")
        }
    ) {
        Scaffold { contentPadding ->
            BackHandler { onBackClicked() }

            TableTaskContent(
                state = state,
                contentPadding = contentPadding,
                onChangeTableClicked = onChangeTableClicked,
            )
        }
    }
}
