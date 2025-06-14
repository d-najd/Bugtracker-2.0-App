package io.dnajd.domain.google_auth.service

import io.dnajd.domain.auth.model.JwtTokenHolder

interface GoogleAuthRepository {
	/**
	 * @param googleToken Should be only the token itself
	 */
	fun googleSignIn(googleToken: String): Result<JwtTokenHolder>

	/**
	 * @param googleToken Should be only the token itself
	 */
	fun googleSignUp(googleToken: String): Result<JwtTokenHolder>
}