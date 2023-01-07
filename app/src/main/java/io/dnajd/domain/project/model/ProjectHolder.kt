package io.dnajd.domain.project.model


import com.google.gson.annotations.SerializedName

data class ProjectHolder(
    @SerializedName("data") val `data`: List<Project>
)