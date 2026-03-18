package com.daedan.festabook.data.datasource.remote.device

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.request.DeviceRegisterRequest
import com.daedan.festabook.data.model.response.DeviceRegisterResponse
import com.daedan.festabook.data.service.DeviceService
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_DEVICE_REGISTER_RESPONSE: DeviceRegisterResponse =
    DeviceRegisterResponse(id = 123)

@Suppress("UNCHECKED_CAST")
private val FAKE_DEVICE_REGISTER_HTTP_RESPONSE: Response<DeviceRegisterResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_DEVICE_REGISTER_RESPONSE,
    ) as Response<DeviceRegisterResponse>

class DeviceDataSourceTest {
    private lateinit var deviceService: DeviceService
    private lateinit var deviceRemoteDataSource: DeviceRemoteDataSource

    @BeforeTest
    fun setUp() {
        deviceService = mock()
        deviceRemoteDataSource = DeviceRemoteDataSourceImpl(deviceService)
    }

    @Test
    fun `디바이스 식별자와 fcmToken으로 디바이스 등록을 할 수 있다`() =
        runTest {
            // given
            val deviceIdentifier = "device-123"
            val fcmToken = "fcm-abc"

            val request =
                DeviceRegisterRequest(
                    deviceIdentifier = deviceIdentifier,
                    fcmToken = fcmToken,
                )

            everySuspend { deviceService.registerDevice(request) } returns FAKE_DEVICE_REGISTER_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_DEVICE_REGISTER_HTTP_RESPONSE }
            val result = deviceRemoteDataSource.registerDevice(deviceIdentifier, fcmToken)

            // then
            verifySuspend { deviceService.registerDevice(request) }
            assertEquals(expected, result)
        }
}
