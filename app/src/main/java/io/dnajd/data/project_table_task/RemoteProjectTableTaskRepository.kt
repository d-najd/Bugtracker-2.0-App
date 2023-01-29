package io.dnajd.data.project_table_task

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.project_table_task.model.ProjectTableTask
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteProjectTableTaskRepository : ProjectTableTaskRepository {
    private val factory: ProjectTableTaskRepositoryApi =
        Injekt.get<Retrofit.Builder>()
            .baseUrl(Urls.apiAppend(Urls.PROJECT_TABLE_TASK_RAW)).build().create(ProjectTableTaskRepositoryApi::class.java)

    override suspend fun get(taskId: Long): ProjectTableTask? =
        factory.get(taskId).processRequest()

    override suspend fun create(task: ProjectTableTask): ProjectTableTask? =
        factory.create(task).processRequest()

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean =
        factory.swapTaskPositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun movePositionTo(fId: Long, sId: Long): Boolean =
        factory.moveTaskPositions(id = fId, sId = sId).processVoidRequest()

}

private interface ProjectTableTaskRepositoryApi {

    @GET("{id}")
    fun get(
        @Path("id") id: Long
    ): Call<ProjectTableTask>

    @POST
    fun create(
        @Body task: ProjectTableTask,
    ): Call<ProjectTableTask>

    @PATCH("{id}/swapPositionWith/{sId}")
    fun swapTaskPositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>

    @PATCH("{id}/movePositionTo/{sId}")
    fun moveTaskPositions(
        @Path("id") id: Long,
        @Path("sId") sId: Long,
    ): Call<Void>

}
