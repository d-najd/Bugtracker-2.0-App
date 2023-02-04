package io.dnajd.data.table_task

import io.dnajd.data.utils.Urls
import io.dnajd.data.utils.processRequest
import io.dnajd.data.utils.processVoidRequest
import io.dnajd.domain.table_task.model.TableTask
import io.dnajd.domain.table_task.service.TableTaskRepository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object RemoteTableTaskRepository : TableTaskRepository {
    private val factory: TableTaskRepositoryApi =
        Injekt.get<Retrofit.Builder>()
            .baseUrl("${Urls.TABLE_TASK.getAppendedUrl()}/").build().create(TableTaskRepositoryApi::class.java)

    override suspend fun get(taskId: Long): TableTask? =
        factory.get(taskId).processRequest()

    override suspend fun create(task: TableTask): TableTask? =
        factory.create(task).processRequest()

    override suspend fun updateNoBody(
        id: Long,
        title: String?,
        description: String?,
        severity: Int?
    ): Boolean = factory.updateNoBody(
        id = id,
        title = title,
        description = description,
        severity = severity
    ).processVoidRequest()

    override suspend fun swapPositionWith(fId: Long, sId: Long): Boolean =
        factory.swapTaskPositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun movePositionTo(fId: Long, sId: Long): Boolean =
        factory.moveTaskPositions(id = fId, sId = sId).processVoidRequest()

    override suspend fun swapTable(id: Long, tableId: Long): Boolean =
        factory.swapTable(id = id, tableId = tableId).processVoidRequest()

}

private interface TableTaskRepositoryApi {

    @GET("{id}")
    fun get(
        @Path("id") id: Long
    ): Call<TableTask>

    @POST(Urls.TABLE_TASK.appendedUrlLocal)
    fun create(
        @Body task: TableTask,
    ): Call<TableTask>

    /**
     * Do not modify [returnBody]
     */
    @PUT("{id}")
    fun updateNoBody(
        @Path("id") id: Long,
        @Query("title") title: String? = null,
        @Query("description") description: String? = null,
        @Query("severity") severity: Int? = null,
        @Query("returnBody") returnBody: Boolean = false,
    ): Call<Void>

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

    @PATCH("{id}/swapTable/{tableId}")
    fun swapTable(
        @Path("id") id: Long,
        @Path("tableId") tableId: Long,
    ): Call<Void>

}
