package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectTableTask(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("parentIssueId") val parentTaskId: Long?,
    @SerializedName("severity") val severity: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("labels") val labels: List<ProjectLabel>,
    @SerializedName("childIssues") val childTasks: List<ProjectTableChildTask>,
)

fun ProjectTableTask.copy(): ProjectTableTask = ProjectTableTask(
    id = this.id,
    title = this.title,
    parentTaskId = this.parentTaskId,
    severity = this.severity,
    position = this.position,
    labels = this.labels.map { it.copy() },
    childTasks = this.childTasks.map { it.copy() }
)