package io.dnajd.domain.google_auth.service

import io.dnajd.domain.auth.model.JwtTokenHolder
import io.dnajd.domain.google_auth.model.CreateUser

interface GoogleAuthRepository {
	/**
	 * @param googleToken Should be only the token itself
	 */
	suspend fun googleSignIn(googleToken: String): Result<JwtTokenHolder>

	/**
	 * @param googleToken Should be only the token itself
	 */
	suspend fun googleSignUp(
		googleToken: String,
		userDto: CreateUser,
	): Result<JwtTokenHolder>
}