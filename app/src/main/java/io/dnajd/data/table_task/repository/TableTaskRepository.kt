package io.dnajd.data.table_task.repository

import io.dnajd.data.utils.RepositoryBase
import io.dnajd.domain.table_task.model.TableTask

object TableTaskRepository :
	RepositoryBase<Set<TableTask>, RepositoryBase.State<Set<TableTask>>>(State(emptySet()))