package com.daedan.festabook.data.datasource.remote.waiting

import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.PlaceWaitingResponse
import com.daedan.festabook.domain.model.WaitingStatus
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

val FAKE_MY_WAITING_RESPONSE =
    MyWaitingResponse(
        waitingId = 1L,
        waitingOrder = 3,
        partySize = 2,
        waitingStatus = WaitingStatus.WAITING,
        totalWaitingTeams = 10,
        estimatedWaitTime = 15,
        phoneNumber = "010-1234-5678",
    )

val FAKE_PLACE_WAITING_RESPONSE =
    PlaceWaitingResponse(
        totalWaitingTeams = 10,
        estimatedWaitTime = 15,
    )

val FAKE_WAITING_REGISTER_REQUEST =
    WaitingRegisterRequest(
        partySize = 2,
    )

@Suppress("UNCHECKED_CAST")
val FAKE_MY_WAITING_RESPONSE_WRAPPED: Response<MyWaitingResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_MY_WAITING_RESPONSE,
    ) as Response<MyWaitingResponse>

@Suppress("UNCHECKED_CAST")
val FAKE_PLACE_WAITING_RESPONSE_WRAPPED: Response<PlaceWaitingResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_PLACE_WAITING_RESPONSE,
    ) as Response<PlaceWaitingResponse>

@Suppress("UNCHECKED_CAST")
val FAKE_CANCEL_WAITING_RESPONSE: Response<Unit> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = Unit,
    ) as Response<Unit>
