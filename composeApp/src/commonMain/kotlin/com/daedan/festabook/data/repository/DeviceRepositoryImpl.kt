package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FcmDataSource
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class DeviceRepositoryImpl(
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val fcmDataSource: FcmDataSource,
) : DeviceRepository {
    override suspend fun registerDevice(
        deviceIdentifier: String,
        fcmToken: String,
    ): Result<Long> {
        val response =
            deviceRemoteDataSource
                .registerDevice(
                    deviceIdentifier = deviceIdentifier,
                    fcmToken = fcmToken,
                ).toResult()
        return response.mapCatching { it.id }
    }

    override suspend fun saveDeviceId(deviceId: Long) {
        deviceLocalDataSource.saveDeviceId(deviceId)
    }

    override suspend fun getUuid(): String? =
        deviceLocalDataSource.getUuid().firstOrNull() ?: run {
            // 로그 달아주세요잉
            null
        }

    override suspend fun getFcmToken(): String? =
        fcmDataSource.getFcmToken().firstOrNull() ?: run {
            // 로그 달아주세요잉
            null
        }

    override suspend fun saveFcmToken(token: String) {
        fcmDataSource.saveFcmToken(token)
    }
}
