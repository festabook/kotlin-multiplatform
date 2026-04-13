package com.daedan.festabook.data.datasource.remote.device

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.response.DeviceRegisterResponse

interface DeviceRemoteDataSource {
    suspend fun registerDevice(
        deviceIdentifier: String,
        fcmToken: String,
    ): ApiResult<DeviceRegisterResponse>

    suspend fun registerPhone(
        deviceId: Long,
        phoneNumber: String,
    ): ApiResult<Unit>

    suspend fun updatePhone(
        deviceId: Long,
        phoneNumber: String,
    ): ApiResult<Unit>
}
