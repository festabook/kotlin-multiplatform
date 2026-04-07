package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.PlaceWaitingResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface WaitingService {
    @GET("waitings/me")
    suspend fun fetchMyWaiting(): Response<MyWaitingResponse>

    @GET("places/{placeId}/waiting-status")
    suspend fun fetchPlaceWaiting(
        @Path("placeId") placeId: Long,
    ): Response<PlaceWaitingResponse>

    @POST("waitings/{waitingId}/cancel")
    suspend fun cancelWaiting(
        @Path("waitingId") waitingId: Long,
    ): Response<Unit>

    @POST("places/{placeId}/waitings")
    suspend fun registerWaiting(
        @Path("placeId") placeId: Long,
        @Body request: WaitingRegisterRequest,
    ): Response<MyWaitingResponse>
}
