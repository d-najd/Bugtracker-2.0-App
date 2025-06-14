package io.dnajd.domain.jwt_auth.service

import io.dnajd.domain.auth.model.JwtTokenHolder

interface JwtAuthPreferenceStore {
	/**
	 * Retrieves available tokens
	 */
	fun retrieveTokenHolder(): JwtTokenHolder {
		return JwtTokenHolder(
			access = retrieveAccessToken(),
			refresh = retrieveRefreshToken(),
		)
	}

	/**
	 * Replaces available tokens
	 */
	fun storeTokenHolder(tokenHolder: JwtTokenHolder) {
		if (tokenHolder.access != null) {
			storeAccessToken(tokenHolder.access)
		}
		if (tokenHolder.refresh != null) {
			storeRefreshToken(tokenHolder.refresh)
		}
	}

	/**
	 * Replaces the access token
	 */
	fun storeAccessToken(token: String)

	/**
	 * Retrieves the access token or returns null if there is none stored
	 */
	fun retrieveAccessToken(): String?

	/**
	 * Replaces the refresh token
	 */
	fun storeRefreshToken(token: String)

	/**
	 * Retrieves the refresh token or returns null if there is none stored
	 */
	fun retrieveRefreshToken(): String?

}