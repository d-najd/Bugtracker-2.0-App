package io.dnajd.data.library

import io.dnajd.data.utils.processRequest
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectHolder
import io.dnajd.domain.project.service.ProjectRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object ProjectRepositoryImpl : ProjectRepository {
    private var factory: LibraryRepositoryApi? = null

    @Synchronized
    private fun getFactory(): LibraryRepositoryApi {
        if(factory == null){
            factory = Injekt.get<Retrofit>().create(LibraryRepositoryApi::class.java)
        }
        return factory!!;
    }

    override suspend fun getProjects(username: String): List<Project> =
        getFactory().getProjectsByUsername(username).processRequest()?.data ?: emptyList()

}

private interface LibraryRepositoryApi {

    @GET("user/{username}")
    fun getProjectsByUsername(@Path("username") username: String): Call<ProjectHolder>

}