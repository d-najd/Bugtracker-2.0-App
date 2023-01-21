package io.dnajd.data.project

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.project.model.Project
import io.dnajd.domain.project.model.ProjectHolder
import io.dnajd.domain.project.service.ProjectRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectRepository : ProjectRepository {
    private var factory: ProjectRepositoryApi = Injekt.get<Retrofit>().create(ProjectRepositoryApi::class.java)

    override suspend fun getAll(username: String): List<Project> =
        factory.getProjectsByUsername(username).processRequest()?.data ?: emptyList()

    override suspend fun get(id: Long): Project? =
        factory.getProjectById(id).processRequest()

    override suspend fun create(project: Project): Project? =
        factory.createProject(project).processRequest()

    override suspend fun changeTitle(id: Long, newTitle: String): Boolean =
        factory.renameProject(id, newTitle).processVoidRequest()

    override suspend fun delete(id: Long): Boolean =
        factory.deleteProject(id).processVoidRequest()

}

private interface ProjectRepositoryApi {

    @GET("${Urls.PROJECT_RAW}/user/{username}")
    fun getProjectsByUsername(@Path("username") username: String): Call<ProjectHolder>

    @GET("${Urls.PROJECT_RAW}/{id}")
    fun getProjectById(@Path("id") id: Long) : Call<Project>

    @POST(Urls.PROJECT_RAW)
    fun createProject(@Body project: Project): Call<Project>

    @PATCH("${Urls.PROJECT_RAW}/{id}/title/{newTitle}")
    fun renameProject(
        @Path("id") id: Long,
        @Path("newTitle") newTitle: String
    ): Call<Void>

    @DELETE("${Urls.PROJECT_RAW}/{id}")
    fun deleteProject(@Path("id") id: Long) : Call<Void>

}