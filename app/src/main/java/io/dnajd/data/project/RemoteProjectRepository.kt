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
    private val factory: ProjectRepositoryApi =
        Injekt.get<Retrofit.Builder>()
            .baseUrl(Urls.apiAppend(Urls.PROJECT_RAW)).build().create(ProjectRepositoryApi::class.java)

    override suspend fun getAll(username: String): List<Project> =
        factory.getProjectsByUsername(username).processRequest()?.data ?: emptyList()

    override suspend fun get(id: Long): Project? =
        factory.getProjectById(id).processRequest()

    override suspend fun create(project: Project): Project? =
        factory.createProject(project).processRequest()

    override suspend fun changeTitle(id: Long, newTitle: String): Boolean =
        factory.updateNoBody(id = id, title = newTitle).processVoidRequest()

    override suspend fun delete(id: Long): Boolean =
        factory.deleteProject(id).processVoidRequest()

}

interface ProjectRepositoryApi {

    @GET("user/{username}")
    fun getProjectsByUsername(@Path("username") username: String): Call<ProjectHolder>

    @GET("{id}")
    fun getProjectById(@Path("id") id: Long) : Call<Project>

    @POST
    fun createProject(@Body project: Project): Call<Project>

    /**
     * @see updateNoBody
     */
    @PUT("{id}")
    fun update(
        @Path("id") id: Long,
        @Query("title") title: String? = null,
        @Query("description") description: String? = null,
    ): Call<Project>

    /**
     * Do not modify [returnBody]
     * @see update
     */
    @PUT("{id}")
    fun updateNoBody(
        @Path("id") id: Long,
        @Query("title") title: String? = null,
        @Query("description") description: String? = null,
        @Query("returnBody") returnBody: Boolean = false,
    ): Call<Void>

    @DELETE("{id}")
    fun deleteProject(@Path("id") id: Long) : Call<Void>

}