package io.dnajd.data.project_table_task

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table_task.model.ProjectTableTaskBasic
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectTableTaskRepository : ProjectTableTaskRepository {
    private var factory: ProjectTableTaskRepositoryApi? = null

    private fun getFactory(): ProjectTableTaskRepositoryApi {
        if (factory == null) {
            factory = Injekt.get<Retrofit>().create(ProjectTableTaskRepositoryApi::class.java)
        }
        return factory!!
    }

    override suspend fun get(taskId: Long): ProjectTableTask? =
        getFactory().get(taskId).processRequest()

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean =
        getFactory().swapTaskPositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun movePositionTo(fId: Long, sId: Long): Boolean =
        getFactory().moveTaskPositions(id = fId, sId = sId).processVoidRequest()

}

private interface ProjectTableTaskRepositoryApi {

    @GET("${Urls.PROJECT_TABLE_TASK_RAW}/{id}")
    fun get(
        @Path("id") id: Long
    ): Call<ProjectTableTask>

    @PATCH("${Urls.PROJECT_TABLE_TASK_RAW}/{id}/swapPositionWith/{sId}")
    fun swapTaskPositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>


    @PATCH("${Urls.PROJECT_TABLE_TASK_RAW}/{id}/movePositionTo/{sId}")
    fun moveTaskPositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>

}
