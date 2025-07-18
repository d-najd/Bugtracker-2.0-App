package io.dnajd.data.utils

import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.jwt_auth.service.JwtRefreshAuthApiService
import io.dnajd.domain.utils.JwtUtil
import io.dnajd.domain.utils.onFailureWithStackTrace
import io.dnajd.util.launchIO
import kotlinx.coroutines.DelicateCoroutinesApi
import logcat.logcat
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * Manages refreshing of access and refresh token
 */
@OptIn(DelicateCoroutinesApi::class) object JwtTokenRefresher {
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get()
	private val jwtRefreshAuthApiService: JwtRefreshAuthApiService = Injekt.get()

	/**
	 * Checks whether the access and refresh token need refreshing, and sets a timer for when the
	 * access token needs refreshing.
	 *
	 * a timer is not set for refreshing the refresh token since it has long expiration window.
	 *
	 * NOTE the timer for access refresh will only get started once and only if the user is not
	 * prompted to re-sign up, this is intentional since no session should (hopefully) last more
	 * than 24h and adding edge cases and loops would make this more complex for no reason
	 *
	 * @param noUserSignUpNeeded means that the refresh token is valid and the user doesn't need to
	 * (re)signup
	 * @param userSignUpNeeded means that the refresh token is no longer valid and the user needs to
	 * re-signup
	 */
	fun checkTokenValidity(
		noUserSignUpNeeded: () -> Unit,
		userSignUpNeeded: () -> Unit,
	) {
		var userSignUpNeededSet = false
		launchIO {
			val tokenHolder = jwtAuthPreferenceStore
				.retrieveTokenHolder()
				.onFailureWithStackTrace {
					logcat { "Can't retrieve token holder to refresh tokens" }
					userSignUpNeededSet = true
					return@launchIO
				}
				.getOrThrow()

			// Refresh token check
			val refreshToken = tokenHolder.refresh()
			val accessToken = tokenHolder.access()
			if (refreshToken == null || accessToken == null) {
				logcat { "token is not set, user will be asked to sign up" }
				userSignUpNeededSet = true
				return@launchIO
			}

			if (!JwtUtil.refreshTokenLongEnough(refreshToken)) {                // User had exited when asked to re-sign up, invalid token is set to not regenerate
				// invalid tokens over and over for no reason
				userSignUpNeededSet = true
				return@launchIO
			}

			if (JwtUtil.refreshTokenNeedsRefresh(refreshToken)) {
				val newTokenHolder = jwtRefreshAuthApiService
					.refreshAccessAndRefreshTokens(refreshToken)
					.onFailureWithStackTrace {
						logcat { "Unable to refresh the refresh token, user will be prompted to re-signup" }
						userSignUpNeededSet = true
						return@launchIO
					}
					.getOrThrow()
				val newRefreshToken = newTokenHolder.refresh()!!

				jwtAuthPreferenceStore
					.storeTokenHolder(newTokenHolder)
					.onFailureWithStackTrace {
						logcat { "Unable to store tokens, was able to access them moments ago... crashing app" }
						throw it
					}

				if (!JwtUtil.refreshTokenLongEnough(newRefreshToken)) {                    // This is an invalid token but it will be set to prevent user from regenerating
					// invalid token over and over when he opens the app and didn't re-sign up
					userSignUpNeededSet = true
					return@launchIO
				} else {                    // Refresh token successfully refreshed along with access token
					noUserSignUpNeeded.invoke()
					return@launchIO
				}
			}

			if (JwtUtil.accessTokenNeedsRefresh(accessToken)) {
				val newTokenHolder = jwtRefreshAuthApiService
					.refreshAccessToken(refreshToken)
					.onFailureWithStackTrace {
						logcat { "Unable to refresh the access token, user will be prompted to re-signup" }
						userSignUpNeededSet = true
						return@launchIO
					}
					.getOrThrow()
				val newAccessToken = newTokenHolder.access()!!

				jwtAuthPreferenceStore
					.storeAccessToken(newAccessToken)
					.onFailureWithStackTrace {
						logcat { "Unable to store tokens, was able to access them moments ago... crashing app" }
						throw it
					}
				noUserSignUpNeeded.invoke()
				return@launchIO
			}

			// All tokens are valid for now
			noUserSignUpNeeded.invoke()
		}.invokeOnCompletion {
			if (userSignUpNeededSet) {
				userSignUpNeeded.invoke()
				return@invokeOnCompletion
			}            // No user sign up needed, regenerate access token after certain time

			val accessToken = jwtAuthPreferenceStore
				.retrieveAccessToken()
				.onFailureWithStackTrace {
					logcat { "Unable to access tokens, was able to access them moments ago... crashing app" }
					throw it
				}
				.getOrThrow()!!

			val accessNeedsRefreshIn =
				(JwtUtil.accessTokenExpirationWithToleranceMilli(accessToken) - System.currentTimeMillis()).coerceAtLeast(0)
			Timer().schedule(accessNeedsRefreshIn) {
				launchIO {
					if (!JwtUtil.accessTokenNeedsRefresh(accessToken)) {
						throw IllegalStateException("Expiration time calculation is wrong")
					}

					// Hours may have passed between the last check and tokens may have changed
					val refreshToken = jwtAuthPreferenceStore
						.retrieveRefreshToken()
						.onFailureWithStackTrace {
							logcat { "Unable to read tokens" }
							return@launchIO
						}
						.getOrThrow()!!

					val newTokenHolder = jwtRefreshAuthApiService
						.refreshAccessToken(refreshToken)
						.onFailureWithStackTrace {
							logcat { "Unable to refresh the access token, it will expire in ${JwtUtil.SECONDS_THRESHOLD_ACCESS_TOKEN} seconds" }
							return@launchIO
						}
						.getOrThrow()

					jwtAuthPreferenceStore
						.storeAccessToken(newTokenHolder.access()!!)
						.onFailureWithStackTrace {
							logcat { "Unable to store newly generated accessToken" }
							return@launchIO
						}
				}
			}
		}
	}
}