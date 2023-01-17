package io.dnajd.domain.project.model


import com.google.gson.annotations.SerializedName
import java.util.Date

data class ProjectHolder(
    @SerializedName("data") val `data`: List<Project> = emptyList()
)

data class Project(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("owner") val owner: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("createdAt") val createdAt: Date = Date(),
)
