package com.daedan.festabook.data.datasource.remote.festival

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.request.FestivalNotificationRequest
import com.daedan.festabook.data.model.response.festival.FestivalNotificationResponse
import com.daedan.festabook.data.service.FestivalNotificationService
import com.daedan.festabook.data.service.platform.festivalNotificationPlatform
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_FESTIVAL_NOTIFICATION_RESPONSE =
    FestivalNotificationResponse(
        festivalNotificationId = 999L,
    )

@Suppress("UNCHECKED_CAST")
private val FAKE_SAVE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE: Response<FestivalNotificationResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_FESTIVAL_NOTIFICATION_RESPONSE,
    ) as Response<FestivalNotificationResponse>

@Suppress("UNCHECKED_CAST")
private val FAKE_DELETE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE: Response<Unit> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = Unit,
    ) as Response<Unit>

class FestivalNotificationRemoteDataSourceTest {
    private lateinit var festivalNotificationService: FestivalNotificationService
    private lateinit var dataSource: FestivalNotificationRemoteDataSource

    @BeforeTest
    fun setUp() {
        festivalNotificationService = mock()
        dataSource = FestivalNotificationRemoteDataSourceImpl(festivalNotificationService)
    }

    @Test
    fun `축제 알림을 저장할 수 있다`() =
        runTest {
            // given
            val festivalId = 10L
            val deviceId = 20L

            val request = FestivalNotificationRequest(deviceId = deviceId)

            everySuspend {
                festivalNotificationService.saveFestivalNotification(
                    festivalId,
                    festivalNotificationPlatform(),
                    request,
                )
            } returns FAKE_SAVE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_SAVE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE }
            val result = dataSource.saveFestivalNotification(festivalId, deviceId)

            // then
            verifySuspend {
                festivalNotificationService.saveFestivalNotification(
                    festivalId,
                    festivalNotificationPlatform(),
                    request,
                )
            }
            assertEquals(expected, result)
        }

    @Test
    fun `축제 알림을 삭제할 수 있다`() =
        runTest {
            // given
            val festivalNotificationId = 999L

            everySuspend {
                festivalNotificationService.deleteFestivalNotification(festivalNotificationId)
            } returns FAKE_DELETE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_DELETE_FESTIVAL_NOTIFICATION_HTTP_RESPONSE }
            val result = dataSource.deleteFestivalNotification(festivalNotificationId)

            // then
            verifySuspend {
                festivalNotificationService.deleteFestivalNotification(festivalNotificationId)
            }
            assertEquals(expected, result)
        }
}
