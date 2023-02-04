package io.dnajd.domain.table_task.model


import com.google.gson.annotations.SerializedName

data class ProjectLabel(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("label") val label: String,
)