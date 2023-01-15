package io.dnajd.domain.project_table_task.model

import com.google.gson.annotations.SerializedName

data class ProjectTableIssueAssigne(
    @SerializedName("issueId") val issueId: Long,
    @SerializedName("assignerUsername") val assignerUsername: String,
    @SerializedName("assignedUsername") val assignedUsername: String,
)
