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
    private var factory: ProjectTableRepositoryApi = Injekt.get<Retrofit>().create(ProjectTableRepositoryApi::class.java)

    override suspend fun getAll(projectId: Long): List<ProjectTable> =
        factory.getTablesByProjectId(projectId).processRequest()?.data ?: emptyList()

    override suspend fun create(table: ProjectTable): ProjectTable? =
        factory.createTable(table).processRequest()

    override suspend fun changeTitle(id: Long, newTitle: String): Boolean =
        factory.renameTable(id, newTitle).processVoidRequest()

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean =
        factory.swapTablePositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun delete(id: Long): Boolean =
        factory.deleteTable(id).processVoidRequest()

}

private interface ProjectTableRepositoryApi {

    @GET("${Urls.PROJECT_TABLE_RAW}/projectId/{projectId}")
    fun getTablesByProjectId(@Path("projectId") projectId: Long): Call<ProjectTableHolder>

    @POST(Urls.PROJECT_TABLE_RAW)
    fun createTable(@Body table: ProjectTable): Call<ProjectTable>

    @PATCH("${Urls.PROJECT_TABLE_RAW}/{id}/title/{newTitle}")
    fun renameTable(
        @Path("id") id: Long,
        @Path("newTitle") newTitle: String
    ): Call<Void>

    @PATCH("${Urls.PROJECT_TABLE_RAW}/{id}/swapPositionWith/{sId}")
    fun swapTablePositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>

    @DELETE("${Urls.PROJECT_TABLE_RAW}/{id}")
    fun deleteTable(@Path("id") id: Long) : Call<Void>
}