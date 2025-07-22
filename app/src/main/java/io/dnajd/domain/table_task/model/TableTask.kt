package io.dnajd.domain.table_task.model

import com.google.gson.annotations.SerializedName
import io.dnajd.data.table_task.repository.TableTaskRepository
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
	/**
	 * Child tasks in [TableTaskRepository] will only include the id to avoid multiple sources of
	 * truth
	 */
	@SerializedName("childIssues") val childTasks: List<TableTask> = emptyList(),
)