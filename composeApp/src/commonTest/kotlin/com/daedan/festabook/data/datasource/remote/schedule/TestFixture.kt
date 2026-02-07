package com.daedan.festabook.data.datasource.remote.schedule

import com.daedan.festabook.data.model.response.schedule.ScheduleDateResponse
import com.daedan.festabook.data.model.response.schedule.ScheduleEventResponse
import com.daedan.festabook.domain.model.ScheduleEventStatus
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

val FAKE_HTTP_RESPONSE =
    mock<HttpResponse> {
        every { status } returns HttpStatusCode.OK
    }

val FAKE_SCHEDULE_EVENTS: List<ScheduleEventResponse> =
    listOf(
        ScheduleEventResponse(
            id = 1L,
            status = ScheduleEventStatus.COMPLETED,
            startTime = "2024-05-10T10:00:00",
            endTime = "2024-05-10T11:30:00",
            title = "팀 주간 회의",
            location = "회의실 A",
        ),
    )

val FAKE_SCHEDULE_DATES: List<ScheduleDateResponse> =
    listOf(
        ScheduleDateResponse(
            id = 101L,
            date = "2024-05-10",
        ),
    )

@Suppress("UNCHECKED_CAST")
val FAKE_SCHEDULE_EVENTS_RESPONSES: Response<List<ScheduleEventResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_SCHEDULE_EVENTS,
    ) as Response<List<ScheduleEventResponse>>

@Suppress("UNCHECKED_CAST")
val FAKE_SCHEDULE_DATES_RESPONSE: Response<List<ScheduleDateResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_SCHEDULE_DATES,
    ) as Response<List<ScheduleDateResponse>>
