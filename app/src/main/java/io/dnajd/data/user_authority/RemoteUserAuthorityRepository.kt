package io.dnajd.data.user_authority

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.domain.user_authority.model.UserAuthority
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteUserAuthorityRepository: UserAuthorityRepository {
	private var factory: UserAuthorityRepositoryApi = Injekt.get<Retrofit>().create(UserAuthorityRepositoryApi::class.java)

	override suspend fun getAllByProjectId(projectId: Long): List<UserAuthority> =
		factory.get(projectId).processRequest() ?: emptyList()
}


private interface UserAuthorityRepositoryApi {

	@GET("${Urls.USER_AUTHORITY_RAW}/projectId/{projectId}")
	fun get(
		@Path("projectId") projectId: Long
	): Call<List<UserAuthority>>

}
