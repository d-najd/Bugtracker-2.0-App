package io.dnajd.data.utils

import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.utils.onFailureWithStackTrace
import okhttp3.Interceptor
import okhttp3.Response
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class JwtAuthenticator(
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get(),
) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val response = chain.request()
		if (response.headers["Authorization"] != null) {
			return chain.proceed(response)
		}

		val accessToken = jwtAuthPreferenceStore
			.retrieveAccessToken()
			.onFailureWithStackTrace {
				return@onFailureWithStackTrace
			}
			.getOrThrow()
		if (accessToken == null) {            // access token is not available at the current moment
			return chain.proceed(response)
		}

		val builder = response.newBuilder()
		builder.addHeader(
			"Authorization",
			"Bearer $accessToken"
		)
		return chain.proceed(builder.build())
	}
}