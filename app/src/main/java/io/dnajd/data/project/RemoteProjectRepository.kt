package io.dnajd.data.project

import io.dnajd.data.utils.Urls
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

object RemoteProjectRepository : ProjectRepository {
    private var factory: ProjectRepositoryApi? = null

    private fun getFactory(): ProjectRepositoryApi {
        if(factory == null){
            factory = Injekt.get<Retrofit>().create(ProjectRepositoryApi::class.java)
        }
        return factory!!;
    }

    override suspend fun getProjects(username: String): List<Project> =
        getFactory().getProjectsByUsername(username).processRequest()?.data ?: emptyList()

}

private interface ProjectRepositoryApi {

    @GET("${Urls.PROJECT_RAW}/user/{username}")
    fun getProjectsByUsername(@Path("username") username: String): Call<ProjectHolder>

}