package io.dnajd.presentation.table_task.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.dnajd.bugtracker.ui.table_task.TableTaskScreenState

@Composable
fun TableTaskBasicInfo(
	state: TableTaskScreenState.Success,
) {
	val task = state.task
	Text(text = "Task id: ${task.id}")
	Text(text = "Table id: ${task.tableId}")
	Text(text = "Title: ${task.title}")
	Text(text = "Description: ${task.description}")
	Text(text = "Position: ${task.position}")
	Text(text = "Comments count: ${task.comments.size}")
	Text(text = "Labels count: ${task.labels.size}")
	Text(text = "Assigned count: ${task.assigned.size}")
	Text(text = "Created At: ${task.createdAt}")
	Text(text = "Updated At: ${task.updatedAt}")
}