package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.device.DeviceRemoteDataSource
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
    private val deviceRemoteDataSource: DeviceRemoteDataSource,
    private val waitingLocalDataSource: WaitingLocalDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
) : WaitingInfoRepository {
    override suspend fun getWaitingInfo(): Result<WaitingInfo?> {
        val phoneNumber =
            waitingLocalDataSource.getPhoneNumber().firstOrNull()
                ?: return Result.success(null)
        return Result.success(WaitingInfo(phoneNumber = phoneNumber))
    }

    override suspend fun saveWaitingInfo(waitingInfo: WaitingInfo): Result<Unit> {
        val deviceId =
            deviceLocalDataSource.getDeviceId().firstOrNull()
                ?: return Result.failure(IllegalStateException("Device ID not found"))
        val apiResult = deviceRemoteDataSource.registerPhone(deviceId, waitingInfo.phoneNumber)
        return persistPhoneIfAcceptable(apiResult, waitingInfo.phoneNumber)
    }

    override suspend fun updateWaitingInfo(waitingInfo: WaitingInfo): Result<Unit> {
        val deviceId =
            deviceLocalDataSource.getDeviceId().firstOrNull()
                ?: return Result.failure(IllegalStateException("Device ID not found"))
        val apiResult = deviceRemoteDataSource.updatePhone(deviceId, waitingInfo.phoneNumber)
        return persistPhoneIfAcceptable(apiResult, waitingInfo.phoneNumber)
    }

    private suspend fun persistPhoneIfAcceptable(
        apiResult: ApiResult<Unit>,
        phoneNumber: String,
    ): Result<Unit> =
        when (apiResult) {
            is ApiResult.Success -> {
                waitingLocalDataSource.savePhoneNumber(phoneNumber)
                Result.success(Unit)
            }
            is ApiResult.ClientError ->
                if (apiResult.code == HTTP_CONFLICT) {
                    // 이미 등록된 번호 — 서버 상태와 로컬 캐시 동기화
                    waitingLocalDataSource.savePhoneNumber(phoneNumber)
                    Result.success(Unit)
                } else {
                    apiResult.toResult()
                }
            else -> apiResult.toResult()
        }

    companion object {
        private const val HTTP_CONFLICT = 409
    }
}
