package io.dnajd.data.jwt_auth

import io.dnajd.data.preference.AndroidPreferenceStore
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class JwtAuthAndroidPreferenceStore(
	private val androidPreferenceStore: AndroidPreferenceStore = Injekt.get(),
) : JwtAuthPreferenceStore {
	companion object {
		private const val ACCESS_TOKEN_PREFERENCE = "jwt_access_token"
		private const val REFRESH_TOKEN_PREFERENCE = "jwt_access_token"
	}

	override fun retrieveAccessToken(): String? {
		val preference = androidPreferenceStore.getString(ACCESS_TOKEN_PREFERENCE)
		if (!preference.isSet()) {
			return null
		}
		return preference.get()
	}

	override fun storeAccessToken(token: String) {
		androidPreferenceStore.getString(ACCESS_TOKEN_PREFERENCE).set(token)
	}

	override fun retrieveRefreshToken(): String? {
		val preference = androidPreferenceStore.getString(REFRESH_TOKEN_PREFERENCE)
		if (!preference.isSet()) {
			return null
		}
		return preference.get()
	}

	override fun storeRefreshToken(token: String) {
		androidPreferenceStore.getString(REFRESH_TOKEN_PREFERENCE).set(token)
	}
}