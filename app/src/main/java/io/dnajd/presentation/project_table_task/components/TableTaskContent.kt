package io.dnajd.presentation.project_table_task.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.dnajd.bugtracker.ui.project_table_task.TableTaskScreenState

@Composable
fun TableTaskContent(
    state: TableTaskScreenState.Success,
    contentPadding: PaddingValues,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        val task = state.task
        Text(text = "Task id: ${task.id}")
        Text(text = "Table id: ${task.tableId}")
        Text(text = "Title: ${task.title}")
        Text(text = "Description: ${task.description}")
        Text(text = "Position: ${task.position}")
        Text(text = "Comments count: ${task.comments.size}")
        Text(text = "Labels count: ${task.labels.size}")
        Text(text = "Created At: ${task.createdAt}")
        Text(text = "Updated At: ${task.updatedAt}")
    }
}
