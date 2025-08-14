package io.dnajd.data.jwt_auth

import com.auth0.android.jwt.JWT
import io.dnajd.data.utils.Urls
import io.dnajd.domain.auth.model.JwtTokenHolder
import io.dnajd.domain.jwt_auth.service.JwtAuthPreferenceStore
import io.dnajd.domain.jwt_auth.service.JwtRefreshAuthApiService
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object JwtRefreshAuthApiServiceImpl : JwtRefreshAuthApiService {
	private val jwtAuthPreferenceStore: JwtAuthPreferenceStore = Injekt.get()

	private val factory: JwtRefreshAuthApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.JWT_REFRESH_AUTH)
		.build()
		.create(JwtRefreshAuthApi::class.java)

	override suspend fun refreshAccessToken(refreshToken: JWT?): Result<JwtTokenHolder> {
		val safeRefreshToken = retrieveSafeRefreshToken(refreshToken)
		safeRefreshToken.onFailure {
			return Result.failure(it)
		}

		return factory.refreshAccessToken("Bearer ${safeRefreshToken.getOrThrow()}")
	}

	override suspend fun refreshAccessAndRefreshTokens(refreshToken: JWT?): Result<JwtTokenHolder> {
		val safeRefreshToken = retrieveSafeRefreshToken(refreshToken)
		safeRefreshToken.onFailure {
			return Result.failure(it)
		}

		return factory.refreshAccessAndRefreshTokens("Bearer ${safeRefreshToken.getOrThrow()}")
	}

	private fun retrieveSafeRefreshToken(refreshToken: JWT?): Result<JWT> {
		return if (refreshToken == null) {
			val internalTokenResult = jwtAuthPreferenceStore.retrieveRefreshToken()
			if (internalTokenResult.isFailure) {
				return Result.failure(internalTokenResult.exceptionOrNull()!!)
			}

			if (internalTokenResult.getOrThrow() == null) {
				return Result.failure(IllegalStateException("There is no refresh token stored or was passed, yet refresh for it was tried"))
			}
			return Result.success(internalTokenResult.getOrThrow()!!)
		} else {
			Result.success(refreshToken)
		}
	}
}

private interface JwtRefreshAuthApi {
	@GET("access_token")
	suspend fun refreshAccessToken(
		@Header("Authorization") authHeader: String,
	): Result<JwtTokenHolder>

	@GET("access_and_refresh_tokens")
	suspend fun refreshAccessAndRefreshTokens(
		@Header("Authorization") authHeader: String,
	): Result<JwtTokenHolder>
}
