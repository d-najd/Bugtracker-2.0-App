package io.dnajd.domain.auth.model

data class JwtTokenHolder(
	val access: String?,
	val refresh: String?,
)