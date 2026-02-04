package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FcmDataSource
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.data.util.withTimeoutOrNullFallback
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

@ContributesBinding(AppScope::class)
@Inject
class DeviceRepositoryImpl(
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val fcmDataSource: FcmDataSource,
    coroutineScope: CoroutineScope,
) : DeviceRepository {
    private val cachedFcmToken: StateFlow<String?> =
        fcmDataSource.getFcmToken().stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    private val cachedUuid: StateFlow<String?> =
        deviceLocalDataSource.getUuid().stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

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

    override suspend fun getUuid(): String? {
        cachedUuid.value?.let { return it }

        return withTimeoutOrNullFallback(
            producer = { cachedUuid.value ?: cachedUuid.filterNotNull().first() },
            onFallback = { /*TODO 로그 */ },
        )
    }

    override suspend fun getFcmToken(): String? {
        cachedFcmToken.value?.let { return it }

        return withTimeoutOrNullFallback(
            producer = { cachedFcmToken.value ?: cachedFcmToken.filterNotNull().first() },
            onFallback = { /*TODO 로그 */ },
        )
    }

    override suspend fun saveFcmToken(token: String) {
        fcmDataSource.saveFcmToken(token)
    }
}
