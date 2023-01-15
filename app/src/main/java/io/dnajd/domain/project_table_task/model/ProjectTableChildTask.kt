package io.dnajd.domain.project_table_task.model


import com.google.gson.annotations.SerializedName


data class ProjectTableChildTask(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("tableId") val tableId: Long,
)

fun ProjectTableChildTask.toBasic(): ProjectTableChildTaskBasic = ProjectTableChildTaskBasic(
    id = id,
)

/**
 * contains only the most basic of fields, this is used for items in the table which does not require
 * that much data
 */
data class ProjectTableChildTaskBasic(
    @SerializedName("id") val id: Long,
)