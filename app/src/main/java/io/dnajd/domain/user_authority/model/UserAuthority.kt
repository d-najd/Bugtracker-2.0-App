package io.dnajd.domain.user_authority.model


import com.google.gson.annotations.SerializedName

data class UserAuthority(
    @SerializedName("username") val username: String,
    @SerializedName("projectId") val projectId: Long,
    @SerializedName("authority") val authority: UserAuthorityType,
)