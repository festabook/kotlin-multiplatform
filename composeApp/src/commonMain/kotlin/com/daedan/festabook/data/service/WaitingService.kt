package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.PlaceWaitingResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface WaitingService {
    @GET("festivals/{festivalId}/waitings/me")
    suspend fun fetchMyWaiting(
        @Path("festivalId") festivalId: Long,
    ): Response<MyWaitingResponse>

    @GET("places/{placeId}/waitings")
    suspend fun fetchPlaceWaiting(
        @Path("placeId") placeId: Long,
    ): Response<PlaceWaitingResponse>

    @DELETE("waitings/{waitingId}")
    suspend fun cancelWaiting(
        @Path("waitingId") waitingId: Long,
    ): Response<Unit>
}
