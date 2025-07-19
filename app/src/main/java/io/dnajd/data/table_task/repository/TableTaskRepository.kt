package io.dnajd.data.table_task.repository

import io.dnajd.domain.table_task.model.TableTask

data class TableTaskRepositoryState(
	val fetchedTasks: Boolean = false,
	val tasks: List<TableTask> = emptyList(),
)

object TableTaskRepository {	/*
	private val _state: MutableStateFlow<ProjectRepositoryState> =
		MutableStateFlow(ProjectRepositoryState())
	val state: StateFlow<ProjectRepositoryState> = _state.asStateFlow()
	 */

}