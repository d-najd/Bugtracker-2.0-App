package io.dnajd.domain.base

/**
 * Nested entities like [io.dnajd.domain.table_task.model.TableTask.childTasks] should not be set to emptyX and repository child repository be
 * used to grab them using the data with matching data from the current entity (its id)
 */
interface BaseApiEntity<T> {
	fun getId(): T
}
