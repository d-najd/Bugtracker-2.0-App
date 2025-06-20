package io.dnajd.data.utils

import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.jwt_auth.service.JwtRefreshAuthRepository
import io.dnajd.domain.utils.JwtUtil
import io.dnajd.domain.utils.onFailureWithStackTrace
import kotlinx.coroutines.runBlocking
import logcat.logcat
import okhttp3.Interceptor
import okhttp3.Response
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * Checks whether the access or refresh token need refreshing and refreshes them if they do.
 */
object JwtAccessTokenRefresher : Interceptor {
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get()
	private val jwtRefreshAuthRepository: JwtRefreshAuthRepository = Injekt.get()

	private var isTokenBeingRefreshed = false

	// TODO this needs testing and finishing
	// TODO is chain.proceed() able to add multiple requests?
	override fun intercept(chain: Interceptor.Chain): Response {
		val response = chain.proceed(chain.request())
		if (isTokenBeingRefreshed) {
			return response
		}

		val accessToken = jwtAuthPreferenceStore
			.retrieveAccessToken()
			.onFailureWithStackTrace { }
			.getOrNull()
		if (accessToken == null || !JwtUtil.accessTokenNeedsRefresh(accessToken)) {
			return response
		}

		// TODO lock can be used here and queue, smtn like mutex, saw found this as well
		// https://stackoverflow.com/questions/31021725/android-okhttp-refresh-expired-token
		runBlocking {
			isTokenBeingRefreshed = true
			val tokenHolder = jwtRefreshAuthRepository.refreshAccessToken(accessToken)
			tokenHolder.onFailureWithStackTrace {
				isTokenBeingRefreshed = false
				return@runBlocking
			}
			jwtAuthPreferenceStore
				.storeAccessToken(
					tokenHolder
						.getOrThrow()
						.access()!!
				)
				.onFailureWithStackTrace { }
				.onSuccess {
					logcat { "Access token was refreshed and saved from the interceptor" }
				}
			isTokenBeingRefreshed = false
		}
		return response
	}
}