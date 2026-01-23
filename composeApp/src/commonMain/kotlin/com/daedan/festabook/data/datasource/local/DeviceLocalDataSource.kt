package com.daedan.festabook.data.datasource.local

interface DeviceLocalDataSource {
    suspend fun saveUuid(uuid: String)

    suspend fun getUuid(): String?

    suspend fun saveDeviceId(deviceId: Long)

    suspend fun getDeviceId(): Long?
}
