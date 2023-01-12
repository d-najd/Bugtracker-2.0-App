package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectLabel(
    @SerializedName("id") val id: Long,
    @SerializedName("label") val label: String,
)