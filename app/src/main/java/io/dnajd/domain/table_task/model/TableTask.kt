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
	@SerializedName("childIssues") val childTasks: List<TableChildTask> = emptyList(),
)

fun TableTask.toBasic(): TableTaskBasic = TableTaskBasic(
	id = id,
	title = title,
	tableId = tableId,
	parentTaskId = parentTaskId,
	severity = severity,
	position = position,
	labels = labels,
	childTasks = childTasks.map { it.toBasic() })

/**
 * TODO deprecate this?
 *
 * contains only the most basic of fields, this is used for items in the table which does not require
 * that much data
 */
data class TableTaskBasic(
	@SerializedName("id") val id: Long = -1,
	@SerializedName("title") val title: String,
	@SerializedName("tableId") val tableId: Long,
	@SerializedName("parentIssueId") val parentTaskId: Long? = null,
	@SerializedName("severity") val severity: Int,
	@SerializedName("position") val position: Int,
	@SerializedName("labels") val labels: List<ProjectLabel> = emptyList(),
	@SerializedName("childIssues") val childTasks: List<ProjectTableChildTaskBasic> = emptyList(),
)
