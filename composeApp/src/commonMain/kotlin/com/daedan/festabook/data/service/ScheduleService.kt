package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.schedule.ScheduleDateResponse
import com.daedan.festabook.data.model.response.schedule.ScheduleEventResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface ScheduleService {
    @GET("event-dates/{eventDateId}/events")
    suspend fun fetchScheduleEventsById(
        @Path("eventDateId") eventDateId: Long,
    ): Response<List<ScheduleEventResponse>>

    @GET("event-dates")
    suspend fun fetchScheduleDates(): Response<List<ScheduleDateResponse>>
}
