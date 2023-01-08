package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectTableTask(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("parentIssueId") val parentIssueId: Long?,
    @SerializedName("severity") val severity: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("labels") val labels: List<ProjectLabel>,
    @SerializedName("childIssues") val childIssues: List<ProjectTableChildTask>,
)