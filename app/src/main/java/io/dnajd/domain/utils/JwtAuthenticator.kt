package io.dnajd.domain.utils

import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class JwtAuthenticator(
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get(),
) : Authenticator {
	override fun authenticate(route: Route?, response: Response): Request? {
		if (response.request.header("Authorization") != null) {
			return null
		}
		println("Authenticating for response: $response")

		val accessToken = jwtAuthPreferenceStore.retrieveAccessToken()
		if (accessToken != null) {
			return response.request.newBuilder()
				.header("Authorization", "Bearer $accessToken")
				.build()
		}
		println("No access token found, authentication failed")
		return response.request
	}
}