package io.dnajd.domain.jwt_auth.service

import com.auth0.android.jwt.JWT
import io.dnajd.domain.auth.model.JwtTokenHolder

/**
 * TODO this doesn't seem like a correct spot to put this?
 */
interface JwtAuthPreferenceStore {
	/**
	 * Retrieves available tokens
	 */
	fun retrieveTokenHolder(): Result<JwtTokenHolder> {
		val accessTokenResult = retrieveAccessToken()
		val refreshTokenResult = retrieveRefreshToken()

		accessTokenResult.onFailure {
			return Result.failure(it)
		}

		refreshTokenResult.onFailure {
			return Result.failure(it)
		}

		return Result.success(
			JwtTokenHolder(
				access = accessTokenResult.getOrNull(),
				refresh = refreshTokenResult.getOrNull(),
			),
		)
	}

	/**
	 * Replaces available tokens, by default ignores tokens that are null
	 */
	fun storeTokenHolder(tokenHolder: JwtTokenHolder): Result<Unit> {
		val access = tokenHolder.access()
		val refresh = tokenHolder.refresh()
		if (access != null) {
			storeAccessToken(access).onFailure {
				return Result.failure(it)
			}
		}
		if (refresh != null) {
			storeRefreshToken(refresh).onFailure {
				return Result.failure(it)
			}
		}
		return Result.success(Unit)
	}

	/**
	 * Replaces the access token
	 */
	fun storeAccessToken(token: JWT): Result<Unit>

	/**
	 * Retrieves the access token or returns null if there is none stored
	 */
	fun retrieveAccessToken(): Result<JWT?>

	/**
	 * Replaces the refresh token
	 */
	fun storeRefreshToken(token: JWT): Result<Unit>

	/**
	 * Retrieves the refresh token or returns null if there is none stored
	 */
	fun retrieveRefreshToken(): Result<JWT?>
}
