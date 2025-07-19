package io.dnajd.data.table_task.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTask

data class TableTaskRepositoryState(
	val fetchedTasks: Boolean = false,
	val tasks: List<TableTask> = emptyList(),
)

object TableTaskRepository :
	RepositoryBase<List<TableTask>, RepositoryBase.State<List<TableTask>>>(State(emptyList()))