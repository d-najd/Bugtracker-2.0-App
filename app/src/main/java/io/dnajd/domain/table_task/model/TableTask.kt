package io.dnajd.domain.table_task.model

import com.google.gson.annotations.SerializedName
import io.dnajd.domain.BaseApiEntity
import io.dnajd.domain.task_assigned.model.TaskAssigned
import io.dnajd.domain.task_comment.model.TaskComment
import io.dnajd.domain.task_label.model.TaskLabel
import java.util.Date


data class TableTaskListResponse(
	@SerializedName("data") val `data`: List<TableTask> = emptyList(),
)

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
	@SerializedName("comments") val comments: List<TaskComment> = emptyList(),
	@SerializedName("labels") val labels: List<TaskLabel> = emptyList(),
	@SerializedName("assigned") val assigned: List<TaskAssigned> = emptyList(),
	@SerializedName("childIssues") val childTasks: List<TableTask> = emptyList(),
) : java.io.Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return id
	}
}
