package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName
import io.dnajd.domain.project_table_task.model.ProjectTableTaskBasic

data class ProjectTableHolder(
    @SerializedName("data") val `data`: List<ProjectTable> = emptyList()
)

data class ProjectTable(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("projectId") val projectId: Long? = null,
    @SerializedName("title") val title: String,
    @SerializedName("position") val position: Int,
    @SerializedName("issues") val tasks: List<ProjectTableTaskBasic> = emptyList(),
) : java.io.Serializable