package io.dnajd.data.project_table

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableHolder
import io.dnajd.domain.project_table.service.ProjectTableRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectTableRepository : ProjectTableRepository {
    private val factory: ProjectTableRepositoryApi =
        Injekt.get<Retrofit.Builder>()
            .baseUrl("${Urls.PROJECT_TABLE.getAppendedUrl()}/").build().create(ProjectTableRepositoryApi::class.java)

    override suspend fun getAll(
        projectId: Long,
        ignoreTasks: Boolean
    ): List<ProjectTable> = factory.getTablesByProjectId(projectId, ignoreTasks).processRequest()?.data ?: emptyList()

    override suspend fun getOne(
        id: Long,
        ignoreTasks: Boolean
    ): ProjectTable? = factory.getById(id, ignoreTasks).processRequest()

    override suspend fun create(table: ProjectTable): ProjectTable? =
        factory.createTable(table).processRequest()

    override suspend fun updateNoBody(
        id: Long,
        title: String?
    ): Boolean = factory.updateNoBody(
        id = id,
        title = title,
    ).processVoidRequest()

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean =
        factory.swapTablePositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun delete(id: Long): Boolean =
        factory.deleteTable(id).processVoidRequest()

}

private interface ProjectTableRepositoryApi {

    @GET("projectId/{projectId}")
    fun getTablesByProjectId(
        @Path("projectId") projectId: Long,
        @Query("ignoreIssues") ignoreTasks: Boolean,
    ): Call<ProjectTableHolder>

    @GET("id/{id}")
    fun getById(
        @Path("id") id: Long,
        @Query("ignoreIssues") ignoreTasks: Boolean,
    ): Call<ProjectTable>

    @POST(Urls.PROJECT_TABLE.appendedUrlLocal)
    fun createTable(@Body table: ProjectTable): Call<ProjectTable>

    /**
     * Do not modify [returnBody]
     */
    @PUT("{id}")
    fun updateNoBody(
        @Path("id") id: Long,
        @Query("title") title: String? = null,
        @Query("returnBody") returnBody: Boolean = false,
    ): Call<Void>

    @PATCH("{id}/swapPositionWith/{sId}")
    fun swapTablePositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>

    @DELETE("{id}")
    fun deleteTable(@Path("id") id: Long) : Call<Void>
}