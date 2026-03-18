package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface DeviceLocalDataSource {
    suspend fun saveUuid(uuid: String)

    fun getUuid(): Flow<String?>

    suspend fun saveDeviceId(deviceId: Long)

    fun getDeviceId(): Flow<Long?>
}
