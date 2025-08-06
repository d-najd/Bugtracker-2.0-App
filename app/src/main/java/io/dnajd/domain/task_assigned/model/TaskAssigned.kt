package io.dnajd.domain.task_assigned.model

import com.google.gson.annotations.SerializedName
import io.dnajd.domain.base.BaseApiEntity
import java.io.Serializable

data class TaskAssigned(
	@SerializedName("issueId") val taskId: Long = -1,
	@SerializedName("assignedUsername") val assignedUsername: String,
	@SerializedName("assignerUsername") val assignerUsername: String,
) : Serializable, BaseApiEntity<TaskAssigned> {
	override fun getId(): TaskAssigned {
		return this
	}
}
