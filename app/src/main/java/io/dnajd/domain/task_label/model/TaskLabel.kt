package io.dnajd.domain.task_label.model

import com.google.gson.annotations.SerializedName
import io.dnajd.domain.BaseApiEntity

data class TaskLabel(
	@SerializedName("id") val id: Long = -1,
	@SerializedName("issueId") val taskId: Long = -1,
	@SerializedName("label") val label: String,
) : java.io.Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return id
	}
}
