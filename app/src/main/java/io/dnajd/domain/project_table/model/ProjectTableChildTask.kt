package io.dnajd.domain.project_table.model


import com.google.gson.annotations.SerializedName

data class ProjectTableChildTask(
    @SerializedName("id") val id: Long,
)

fun ProjectTableChildTask.copy(): ProjectTableChildTask = ProjectTableChildTask(id = this.id)