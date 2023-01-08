package io.dnajd.domain.project.model


import com.google.gson.annotations.SerializedName
import java.util.Date


data class ProjectHolder(
    @SerializedName("data") val `data`: List<Project>
)

data class Project(
    @SerializedName("id") val id: Long,
    @SerializedName("owner") val owner: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("createdAt") val createdAt: Date,
)