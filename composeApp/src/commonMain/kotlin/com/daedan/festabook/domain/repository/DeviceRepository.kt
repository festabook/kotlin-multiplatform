package com.daedan.festabook.domain.repository

interface DeviceRepository {
    suspend fun registerDevice(
        deviceIdentifier: String,
        fcmToken: String,
    ): Result<Long>

    suspend fun saveDeviceId(deviceId: Long)

    suspend fun getUuid(): String?

    suspend fun getFcmToken(): String?

    suspend fun saveFcmToken(token: String)
}
