package io.dnajd.domain.jwt_auth.service

import io.dnajd.domain.auth.model.JwtTokenHolder

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
			)
		)
	}

	/**
	 * Replaces available tokens, by default ignores tokens that are null
	 */
	fun storeTokenHolder(tokenHolder: JwtTokenHolder): Result<Unit> {
		if (tokenHolder.access != null) {
			storeAccessToken(tokenHolder.access).onFailure {
				return Result.failure(it)
			}
		}
		if (tokenHolder.refresh != null) {
			storeRefreshToken(tokenHolder.refresh).onFailure {
				return Result.failure(it)
			}
		}
		return Result.success(Unit)
	}

	/**
	 * Replaces the access token
	 */
	fun storeAccessToken(token: String): Result<Unit>

	/**
	 * Retrieves the access token or returns null if there is none stored
	 */
	fun retrieveAccessToken(): Result<String?>

	/**
	 * Replaces the refresh token
	 */
	fun storeRefreshToken(token: String): Result<Unit>

	/**
	 * Retrieves the refresh token or returns null if there is none stored
	 */
	fun retrieveRefreshToken(): Result<String?>
}