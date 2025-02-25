package io.dnajd.domain.table_task.model

import com.google.gson.annotations.SerializedName

data class TableTaskAssignee(
	@SerializedName("assignedUsername") val assignedUsername: String,
	@SerializedName("assignerUsername") val assignerUsername: String,
)
