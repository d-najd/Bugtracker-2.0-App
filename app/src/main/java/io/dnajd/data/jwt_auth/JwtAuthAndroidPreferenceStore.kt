package io.dnajd.data.jwt_auth

import com.auth0.android.jwt.JWT
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.preference.service.PreferenceStore
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class JwtAuthAndroidPreferenceStore(
	private val preferenceStore: PreferenceStore = Injekt.get(),
) : JwtAuthPreferenceStore {
	companion object {
		private const val ACCESS_TOKEN_PREFERENCE = "jwt_access_token"
		private const val REFRESH_TOKEN_PREFERENCE = "jwt_refresh_token"
	}

	override fun retrieveAccessToken(): Result<JWT?> {
		val preference = preferenceStore.getString(ACCESS_TOKEN_PREFERENCE)
		if (!preference.isSet()) {
			return Result.success(null)
		}
		return Result.success(JWT(preference.get()))
	}

	override fun storeAccessToken(token: JWT): Result<Unit> {
		preferenceStore
			.getString(ACCESS_TOKEN_PREFERENCE)
			.set(token.toString())
		return Result.success(Unit)
	}

	override fun retrieveRefreshToken(): Result<JWT?> {
		val preference = preferenceStore.getString(REFRESH_TOKEN_PREFERENCE)
		if (!preference.isSet()) {
			return Result.success(null)
		}
		return Result.success(JWT(preference.get()))
	}

	override fun storeRefreshToken(token: JWT): Result<Unit> {
		preferenceStore
			.getString(REFRESH_TOKEN_PREFERENCE)
			.set(token.toString())
		return Result.success(Unit)
	}
}