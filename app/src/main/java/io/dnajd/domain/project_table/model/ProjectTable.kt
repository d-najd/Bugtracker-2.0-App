package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName
import io.dnajd.domain.table_task.model.TableTaskBasic

data class ProjectTableListResponse(
	@SerializedName("data") val `data`: List<ProjectTable> = emptyList(),
)

data class ProjectTable(
	@SerializedName("id") val id: Long = -1L,
	@SerializedName("projectId") val projectId: Long = -1L,
	@SerializedName("title") val title: String = "",
	@SerializedName("position") val position: Int = -1,
	@SerializedName("issues") val tasks: List<TableTaskBasic> = mutableListOf(),
) : java.io.Serializable