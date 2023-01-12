package io.dnajd.data.project_table

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.project_table.model.ProjectTable
import io.dnajd.domain.project_table.model.ProjectTableHolder
import io.dnajd.domain.project_table.service.ProjectTableRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectTableRepository : ProjectTableRepository {
    private var factory: ProjectTableRepositoryApi? = null

    private fun getFactory(): ProjectTableRepositoryApi {
        if(factory == null){
            factory = Injekt.get<Retrofit>().create(ProjectTableRepositoryApi::class.java)
        }
        return factory!!
    }

    override suspend fun getTables(projectId: Long): List<ProjectTable> =
        getFactory().getTablesByProjectId(projectId).processRequest()?.data ?: emptyList()

    override suspend fun renameTable(id: Long, newTitle: String): Boolean =
        getFactory().renameTable(id, newTitle).processVoidRequest()

    override suspend fun swapTablePositions(fId: Long, sId: Long): Boolean =
        getFactory().swapTablePositions(id = fId, sId = sId).processVoidRequest()

}

private interface ProjectTableRepositoryApi {

    @GET("${Urls.PROJECT_TABLE_RAW}/projectId/{projectId}")
    fun getTablesByProjectId(@Path("projectId") projectId: Long): Call<ProjectTableHolder>

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

}