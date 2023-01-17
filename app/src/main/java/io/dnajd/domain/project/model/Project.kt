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
    @SerializedName("description") val description: String? = null,
    @SerializedName("createdAt") val createdAt: Date,
) {
    companion object {
        fun apiBase(
            id: Long = -1,
            owner: String,
            title: String,
            description: String? = null,
            createdAt: Date = Date(),
        ): Project = Project(
            id = id,
            owner = owner,
            title = title,
            description = description,
            createdAt = createdAt,
        )
    }
}
