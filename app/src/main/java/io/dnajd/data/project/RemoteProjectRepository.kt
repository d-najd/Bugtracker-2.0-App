package io.dnajd.data.project

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectHolder
import io.dnajd.domain.project.service.ProjectRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectRepository : ProjectRepository {
    private var factory: ProjectRepositoryApi = Injekt.get<Retrofit>().create(ProjectRepositoryApi::class.java)

    override suspend fun getAll(username: String): List<Project> =
        factory.getProjectsByUsername(username).processRequest()?.data ?: emptyList()

    override suspend fun create(project: Project): Project? =
        factory.createProject(project).processRequest()

}

private interface ProjectRepositoryApi {

    @GET("${Urls.PROJECT_RAW}/user/{username}")
    fun getProjectsByUsername(@Path("username") username: String): Call<ProjectHolder>

    @POST(Urls.PROJECT_RAW)
    fun createProject(@Body project: Project): Call<Project>

}