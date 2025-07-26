package io.dnajd.data.utils

import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import okhttp3.Interceptor
import okhttp3.Response
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object JwtAuthenticator : Interceptor {
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get()

	override fun intercept(chain: Interceptor.Chain): Response {
		val response = chain.request()
		if (response.headers["Authorization"] != null) {
			return chain.proceed(response)
		}

		val accessToken = jwtAuthPreferenceStore
			.retrieveAccessToken()
			.getOrThrow()
		if (accessToken == null) { // No access token set
			return chain.proceed(response)
		}

		val builder = response.newBuilder()
		builder.addHeader(
			"Authorization",
			"Bearer $accessToken",
		)
		return chain.proceed(builder.build())
	}
}
