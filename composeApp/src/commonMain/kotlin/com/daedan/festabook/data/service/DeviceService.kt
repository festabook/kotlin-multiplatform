package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.request.DeviceRegisterRequest
import com.daedan.festabook.data.model.request.PhoneRegisterRequest
import com.daedan.festabook.data.model.response.DeviceRegisterResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface DeviceService {
    @POST("devices")
    suspend fun registerDevice(
        @Body deviceRegisterRequest: DeviceRegisterRequest,
    ): Response<DeviceRegisterResponse>

    @POST("devices/{deviceId}/phone")
    suspend fun registerPhone(
        @Path("deviceId") deviceId: Long,
        @Body request: PhoneRegisterRequest,
    ): Response<Unit>

    @PUT("devices/{deviceId}/phone")
    suspend fun updatePhone(
        @Path("deviceId") deviceId: Long,
        @Body request: PhoneRegisterRequest,
    ): Response<Unit>
}
