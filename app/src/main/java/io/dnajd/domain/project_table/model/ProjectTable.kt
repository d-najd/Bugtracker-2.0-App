package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName
import io.dnajd.domain.base.BaseApiEntity
import io.dnajd.domain.table_task.model.TableTask

data class ProjectTableListResponse(
	@SerializedName("data") val `data`: List<ProjectTable> = emptyList(),
)

/**
 * Used primarily for networking
 */
data class ProjectTable(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("projectId") val projectId: Long = -1L,
	@SerializedName("title") val title: String = "",
	@SerializedName("position") val position: Int = -1,
	@SerializedName("issues") val tasks: List<TableTask> = listOf(),
) : java.io.Serializable, BaseApiEntity<Long> {
	override fun getId(): Long {
		return id
	}
}
