package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class WaitingInfoRepositoryImpl(
    private val waitingRemoteDataSource: WaitingRemoteDataSource,
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
) : WaitingInfoRepository {
    override suspend fun getWaitingInfo(): Result<WaitingInfo?> =
        when (val apiResult = waitingRemoteDataSource.fetchMyWaiting()) {
            is ApiResult.ClientError ->
                if (apiResult.code == 404) {
                    Result.success(null)
                } else {
                    apiResult.toResult()
                }
            else -> apiResult.toResult().mapCatching { WaitingInfo(phoneNumber = it.phoneNumber) }
        }

    override suspend fun saveWaitingInfo(waitingInfo: WaitingInfo): Result<Unit> {
        val deviceId =
            deviceLocalDataSource.getDeviceId().firstOrNull()
                ?: return Result.failure(IllegalStateException("Device ID not found"))
        return deviceRemoteDataSource.registerPhone(deviceId, waitingInfo.phoneNumber).toResult()
    }
}
