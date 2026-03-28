package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.request.FestivalNotificationRequest
import com.daedan.festabook.data.model.response.festival.FestivalNotificationResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface FestivalNotificationService {
    @POST("festivals/{festivalId}/notifications/{platform}")
    suspend fun saveFestivalNotification(
        @Path("festivalId") id: Long,
        @Path("platform") platform: String,
        @Body request: FestivalNotificationRequest,
    ): Response<FestivalNotificationResponse>

    @DELETE("festivals/notifications/{festivalNotificationId}")
    suspend fun deleteFestivalNotification(
        @Path("festivalNotificationId") id: Long,
    ): Response<Unit>
}
