package io.dnajd.domain.table_task.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TableTaskComment(
    @SerializedName("id") val id: Long = -1,
    @SerializedName("user") val user: String,
    @SerializedName("message") val message: String,
    @SerializedName("createdAt") val createdAt: Date = Date(),
    @SerializedName("editedAt") val editedAt: Date? = null,
)
