package io.dnajd.domain.task_comment.model

import com.google.gson.annotations.SerializedName
import io.dnajd.domain.base.BaseApiEntity
import java.util.Date

data class TaskComment(
	@SerializedName("id") val id: Long = -1,
	@SerializedName("issueId") val taskId: Long = -1,
	@SerializedName("user") val user: String = "",
	@SerializedName("message") val message: String = "",
	@SerializedName("createdAt") val createdAt: Date = Date(),
	@SerializedName("editedAt") val editedAt: Date? = null,
) : java.io.Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return id
	}
}
