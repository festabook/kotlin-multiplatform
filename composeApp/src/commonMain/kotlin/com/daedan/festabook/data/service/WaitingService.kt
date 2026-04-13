package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.WaitingExistResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface WaitingService {
    @GET("waitings/me")
    suspend fun fetchMyWaiting(): Response<MyWaitingResponse>

    @GET("waitings/me/exists")
    suspend fun fetchMyWaitingExist(): Response<WaitingExistResponse>

    @POST("waitings/{waitingId}/cancel")
    suspend fun cancelWaiting(
        @Path("waitingId") waitingId: Long,
    ): Response<Unit>
}
