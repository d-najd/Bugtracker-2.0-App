package io.dnajd.domain.auth.model

import com.auth0.android.jwt.JWT
import com.google.gson.annotations.SerializedName

data class JwtTokenHolder(
	@SerializedName("access") private val _access: String?,
	@SerializedName("refresh") private val _refresh: String?,
) {
	constructor(
		access: JWT?,
		refresh: JWT?,
	) : this(
		access?.toString(),
		refresh?.toString(),
	)

	fun access(): JWT? {
		if (_access == null) {
			return null
		}
		return JWT(_access)
	}

	fun refresh(): JWT? {
		if (_refresh == null) {
			return null
		}
		return JWT(_refresh)
	}
}
