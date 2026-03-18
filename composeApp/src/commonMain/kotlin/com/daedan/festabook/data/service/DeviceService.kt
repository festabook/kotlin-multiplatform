package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.request.DeviceRegisterRequest
import com.daedan.festabook.data.model.response.DeviceRegisterResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface DeviceService {
    @POST("devices")
    suspend fun registerDevice(
        @Body deviceRegisterRequest: DeviceRegisterRequest,
    ): Response<DeviceRegisterResponse>
}
