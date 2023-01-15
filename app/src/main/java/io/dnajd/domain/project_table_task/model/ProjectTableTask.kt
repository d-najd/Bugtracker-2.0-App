package io.dnajd.domain.project_table_task.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProjectTableTask (
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("tableId") val tableId: Long,
    @SerializedName("reporter") val reporter: String,
    @SerializedName("parentIssueId") val parentTaskId: Long?,
    @SerializedName("severity") val severity: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("updatedAt") val updatedAt: Date?,
    @SerializedName("comments") val comments: List<ProjectTableTaskComment>,
    @SerializedName("labels") val labels: List<ProjectLabel>,
    @SerializedName("assigned") val assigned: List<ProjectTableIssueAssigne>,
    @SerializedName("childIssues") val childTasks: List<ProjectTableChildTask>
)

fun ProjectTableTask.toBasic(): ProjectTableTaskBasic = ProjectTableTaskBasic(
    id = id,
    title = title,
    tableId = tableId,
    parentTaskId = parentTaskId,
    severity = severity,
    position = position,
    labels = labels,
    childTasks = childTasks.map { it.toBasic() }
)

/**
 * contains only the most basic of fields, this is used for items in the table which does not require
 * that much data
 */
data class ProjectTableTaskBasic(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("tableId") val tableId: Long,
    @SerializedName("parentIssueId") val parentTaskId: Long?,
    @SerializedName("severity") val severity: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("labels") val labels: List<ProjectLabel>,
    @SerializedName("childIssues") val childTasks: List<ProjectTableChildTaskBasic>,
)
