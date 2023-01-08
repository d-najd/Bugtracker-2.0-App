package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectTableHolder(
    @SerializedName("data") val `data`: List<ProjectTable>
)

data class ProjectTable(
    @SerializedName("id") val id: Long,
    @SerializedName("projectId") val projectId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("position") val position: Int
)