package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectTableHolder(
    @SerializedName("data") val `data`: List<ProjectTable>
)

data class ProjectTable(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("position") val position: Int,
    @SerializedName("issues") val tasks: List<ProjectTableTask>,
)

fun ProjectTable.copy(): ProjectTable = ProjectTable(
    id = this.id,
    title = this.title,
    position = this.position,
    tasks = this.tasks.map { it.copy() }
)