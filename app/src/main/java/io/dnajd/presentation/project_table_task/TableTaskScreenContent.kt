package io.dnajd.presentation.project_table_task

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState
import io.dnajd.presentation.project_table_task.content.TableTaskContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableTaskScreenContent(
    state: TableTaskScreenState.Success,
    onBackClicked: () -> Unit,
) {
    Scaffold { contentPadding ->
        BackHandler { onBackClicked() }

        TableTaskContent(
            state = state,
            contentPadding = contentPadding,
        )
    }
}
