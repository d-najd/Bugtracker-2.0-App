package io.dnajd.domain.table_task.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TableTask(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("title") val title: String = "",
	@SerializedName("tableId") val tableId: Long = -1L,
	@SerializedName("reporter") val reporter: String = "",
	@SerializedName("parentIssueId") val parentTaskId: Long? = null,
	@SerializedName("severity") val severity: Int = 1,
	@SerializedName("position") val position: Int = -1,
	@SerializedName("description") val description: String? = null,
	@SerializedName("createdAt") val createdAt: Date = Date(),
	@SerializedName("updatedAt") val updatedAt: Date? = null,
	@SerializedName("comments") val comments: List<TableTaskComment> = emptyList(),
	@SerializedName("labels") val labels: List<ProjectLabel> = emptyList(),
	@SerializedName("assigned") val assigned: List<TableTaskAssignee> = emptyList(),
	@SerializedName("childIssues") val childTasks: List<TableTask> = emptyList(),
)

data class TableTaskBasic(
	val id: Long = -1L,
	val title: String = "",
	val tableId: Long = -1L,
	val reporter: String = "",
	val parentTaskId: Long? = null,
	val severity: Int = 1,
	val position: Int = -1,
	val description: String? = null,
	val createdAt: Date = Date(),
	val updatedAt: Date? = null,
	val comments: List<TableTaskComment> = emptyList(),
	val labels: List<ProjectLabel> = emptyList(),
	val assigned: List<TableTaskAssignee> = emptyList(),
	val childTasks: List<Long> = emptyList(),
) {
	constructor(task: TableTask) : this(
		id = task.id,
		title = task.title,
		tableId = task.tableId,
		reporter = task.reporter,
		parentTaskId = task.parentTaskId,
		severity = task.severity,
		position = task.position,
		description = task.description,
		createdAt = task.createdAt,
		updatedAt = task.updatedAt,
		comments = task.comments,
		labels = task.labels,
		assigned = task.assigned,
		childTasks = task.childTasks.map { it.id })
}