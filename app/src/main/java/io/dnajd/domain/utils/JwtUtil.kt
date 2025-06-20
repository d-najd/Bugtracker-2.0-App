package io.dnajd.domain.utils

import com.auth0.android.jwt.JWT
import java.sql.Date

/**
 * Tokens are refreshed before they exceed certain threshold
 * E.X token expires in 3 days and threshold is 1 day, so it should be refreshed in 2 days
 */
object JwtUtil {
	private const val SECONDS_THRESHOLD_ACCESS_TOKEN = 60 * 60 * 1L // 1 hours
	private const val SECONDS_THRESHOLD_REFRESH_TOKEN = 60 * 60 * 24 * 7L // 7 days

	/**
	 * The refresh token may expire in shorter time than [SECONDS_THRESHOLD_REFRESH_TOKEN] which
	 * will cause issues since the token will try to regenerate itself till it crashes the app,
	 * this is a check to prevent that.
	 */
	fun refreshTokenLongEnough(jwt: JWT): Boolean {
		val expiresAt = jwt.expiresAt!!
			.toInstant()
			.toEpochMilli()
		val issuedAt = jwt.issuedAt!!
			.toInstant()
			.toEpochMilli()

		return expiresAt - issuedAt > SECONDS_THRESHOLD_REFRESH_TOKEN
	}

	/**
	 * @see refreshTokenLongEnough if not valid will throw [IllegalArgumentException]
	 */
	@Throws(IllegalArgumentException::class)
	fun refreshTokenNeedsRefresh(jwt: JWT): Boolean {
		if (!refreshTokenLongEnough(jwt)) {
			throw IllegalArgumentException("Token expires in very short time, new refresh token is through oauth token")
		}

		val refreshWithThreshold = Date(
			jwt.expiresAt!!
				.toInstant()
				.minusSeconds(SECONDS_THRESHOLD_REFRESH_TOKEN)
				.toEpochMilli()
		)

		return refreshWithThreshold
			.toInstant()
			.toEpochMilli() > System.currentTimeMillis()
	}

	fun accessTokenNeedsRefresh(jwt: JWT): Boolean {
		val accessWithThreshold = Date(
			jwt.expiresAt!!
				.toInstant()
				.minusSeconds(SECONDS_THRESHOLD_ACCESS_TOKEN)
				.toEpochMilli()
		)

		return accessWithThreshold
			.toInstant()
			.toEpochMilli() > System.currentTimeMillis()
	}

	fun isRefreshTokenValid(jwt: JWT): Boolean {
		return isTokenValid(jwt)
	}

	fun isAccessTokenValid(jwt: JWT): Boolean {
		return isTokenValid(jwt)
	}

	private fun isTokenValid(jwt: JWT): Boolean {
		return !jwt.isExpired(0L)
	}
}