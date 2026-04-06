package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.place.PlaceDetailResponse
import com.daedan.festabook.data.model.response.place.PlaceGeographyResponse
import com.daedan.festabook.data.model.response.place.PlaceResponse
import com.daedan.festabook.data.model.response.place.TimeTagResponse
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface PlaceService {
    @GET("time-tags")
    suspend fun fetchTimeTag(): Response<List<TimeTagResponse>>

    @GET("places/previews")
    suspend fun fetchPlaces(): Response<List<PlaceResponse>>

    @GET("places/{placeId}")
    suspend fun fetchPlaceDetail(
        @Path("placeId") id: Long,
    ): Response<PlaceDetailResponse>

    @GET("places/geographies")
    suspend fun fetchPlaceGeographies(): Response<List<PlaceGeographyResponse>>

    @POST("places/{placeId}/waitings")
    suspend fun registerWaiting(
        @Path("placeId") placeId: Long,
        @Body request: WaitingRegisterRequest,
    ): Response<MyWaitingResponse>
}
