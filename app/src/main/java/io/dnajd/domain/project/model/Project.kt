package io.dnajd.domain.project.model


import com.google.gson.annotations.SerializedName
import io.dnajd.domain.base.BaseApiEntity
import java.util.Date

data class ProjectListResponse(
	@SerializedName("data") val `data`: List<Project> = emptyList(),
)

data class Project(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("owner") val owner: String? = null,
	@SerializedName("title") val title: String = "",
	@SerializedName("description") val description: String? = null,
	@SerializedName("createdAt") val createdAt: Date = Date(),
) : java.io.Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return id
	}
}
