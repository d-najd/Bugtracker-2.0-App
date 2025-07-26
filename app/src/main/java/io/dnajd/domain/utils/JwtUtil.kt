package io.dnajd.domain.utils

import com.auth0.android.jwt.JWT
import io.dnajd.domain.utils.JwtUtil.SECONDS_THRESHOLD_REFRESH_TOKEN
import io.dnajd.domain.utils.JwtUtil.refreshTokenLongEnough

/**
 * Tokens are refreshed before they exceed certain threshold
 * E.X token expires in 3 days and threshold is 1 day, so it should be refreshed in 2 days
 */
object JwtUtil {
	const val SECONDS_THRESHOLD_ACCESS_TOKEN = 60 * 60 * 1L // 1 hours

	@Suppress("MemberVisibilityCanBePrivate")
	const val SECONDS_THRESHOLD_REFRESH_TOKEN = 60 * 60 * 24 * 15L // 15 days

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

		return System.currentTimeMillis() > refreshTokenExpirationWithToleranceMilli(jwt)
	}

	fun accessTokenNeedsRefresh(jwt: JWT): Boolean {
		return System.currentTimeMillis() > accessTokenExpirationWithToleranceMilli(jwt)
	}

	fun accessTokenExpirationWithToleranceMilli(jwt: JWT): Long = jwt.expiresAt!!
		.toInstant()
		.minusSeconds(SECONDS_THRESHOLD_ACCESS_TOKEN)
		.toEpochMilli()

	@Suppress("MemberVisibilityCanBePrivate")
	fun refreshTokenExpirationWithToleranceMilli(jwt: JWT): Long = jwt.expiresAt!!
		.toInstant()
		.minusSeconds(SECONDS_THRESHOLD_REFRESH_TOKEN)
		.toEpochMilli()
}
