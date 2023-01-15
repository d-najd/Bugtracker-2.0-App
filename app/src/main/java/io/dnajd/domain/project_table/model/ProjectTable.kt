package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName
import io.dnajd.domain.project_table_task.model.ProjectTableTaskBasic

data class ProjectTableHolder(
    @SerializedName("data") val `data`: List<ProjectTable>
)

data class ProjectTable(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("position") val position: Int,
    @SerializedName("issues") val tasks: List<ProjectTableTaskBasic>,
)