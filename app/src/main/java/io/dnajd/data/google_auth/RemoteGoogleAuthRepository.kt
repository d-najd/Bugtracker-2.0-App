package io.dnajd.data.google_auth

import io.dnajd.data.utils.Urls
import io.dnajd.domain.auth.model.JwtTokenHolder
import io.dnajd.domain.google_auth.model.CreateUser
import io.dnajd.domain.google_auth.service.GoogleAuthRepository
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteGoogleAuthRepository : GoogleAuthRepository {
	private val factory: GoogleAuthRepositoryApi = Injekt
		.get<Retrofit.Builder>()
		.baseUrl(Urls.GOOGLE_AUTH)
		.build()
		.create(GoogleAuthRepositoryApi::class.java)

	override suspend fun googleSignIn(googleToken: String): Result<JwtTokenHolder> =
		factory.googleSignIn("Bearer $googleToken")

	override suspend fun googleSignUp(
		googleToken: String,
		userInfo: CreateUser,
	): Result<JwtTokenHolder> = factory.googleSignUp(
		"Bearer $googleToken",
		userInfo
	)
}

private interface GoogleAuthRepositoryApi {
	/**
	 * @param authHeader should be the google OAuth 2.0 token like "Bearer $[authHeader]"
	 */
	@GET("./")
	suspend fun googleSignIn(
		@Header("Authorization") authHeader: String,
	): Result<JwtTokenHolder>

	/**
	 * @param authHeader should be the google OAuth 2.0 token like "Bearer $[authHeader]"
	 */
	@POST("./")
	suspend fun googleSignUp(
		@Header("Authorization") authHeader: String,
		@Body userInfo: CreateUser,
	): Result<JwtTokenHolder>
}
