package io.dnajd.domain.project_table_task.model

import com.google.gson.annotations.SerializedName

data class ProjectTableIssueAssignee(
    @SerializedName("assignedUsername") val assignedUsername: String,
    @SerializedName("assignerUsername") val assignerUsername: String,
)