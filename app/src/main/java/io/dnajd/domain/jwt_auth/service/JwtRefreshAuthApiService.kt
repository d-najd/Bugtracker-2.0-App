package io.dnajd.domain.jwt_auth.service

import com.auth0.android.jwt.JWT
import io.dnajd.domain.auth.model.JwtTokenHolder

interface JwtRefreshAuthApiService {
	/**
	 * @param refreshToken if specified this token will be used, otherwise previously stored token
	 * will be retrieved
	 *
	 * access token will always be set
	 */
	suspend fun refreshAccessToken(refreshToken: JWT? = null): Result<JwtTokenHolder>

	/**
	 * @param refreshToken if specified this token will be used, otherwise previously stored token
	 * will be retrieved
	 *
	 * access and refresh tokens will always be set
	 */
	suspend fun refreshAccessAndRefreshTokens(refreshToken: JWT? = null): Result<JwtTokenHolder>
}