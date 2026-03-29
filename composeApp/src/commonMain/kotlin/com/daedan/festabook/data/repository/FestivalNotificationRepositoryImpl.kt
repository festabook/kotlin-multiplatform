package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalNotificationLocalDataSource
import com.daedan.festabook.data.datasource.remote.festival.FestivalNotificationRemoteDataSource
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.data.util.withTimeoutOrNullFallback
import com.daedan.festabook.domain.repository.FestivalNotificationRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@ContributesBinding(AppScope::class)
@Inject
class FestivalNotificationRepositoryImpl(
    private val festivalNotificationRemoteDataSource: FestivalNotificationRemoteDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
    private val festivalNotificationLocalDataSource: FestivalNotificationLocalDataSource,
    private val festivalLocalDataSource: FestivalLocalDataSource,
) : FestivalNotificationRepository {
    override suspend fun saveFestivalNotification(): Result<Unit> {
        val deviceId =
            withTimeoutOrNullFallback(
                producer = { deviceLocalDataSource.getDeviceId().firstOrNull() },
                onFallback = { /*TODO 로그 */ },
            ) ?: return Result.failure(IllegalStateException("deviceId가 null"))

        val festivalId =
            withTimeoutOrNullFallback(
                producer = { festivalLocalDataSource.getFestivalId().firstOrNull() },
                onFallback = { /*TODO 로그 */ },
            ) ?: return Result.failure(IllegalStateException("festivalId가 null"))

        val result =
            festivalNotificationRemoteDataSource
                .saveFestivalNotification(
                    festivalId = festivalId,
                    deviceId = deviceId,
                ).toResult()

        return result
            .mapCatching {
                festivalNotificationLocalDataSource.saveFestivalNotificationId(
                    festivalId,
                    it.festivalNotificationId,
                )
            }
    }

    override suspend fun deleteFestivalNotification(): Result<Unit> {
        val festivalId =
            withTimeoutOrNullFallback(
                producer = { festivalLocalDataSource.getFestivalId().firstOrNull() },
                onFallback = { /*TODO 로그 */ },
            ) ?: return Result.failure(IllegalStateException())

        val festivalNotificationId =
            withTimeoutOrNullFallback(
                producer = {
                    festivalNotificationLocalDataSource
                        .getFestivalNotificationId(festivalId)
                        .firstOrNull()
                },
                onFallback = { /*TODO 로그 */ },
            ) ?: return Result.failure(IllegalStateException())

        return festivalNotificationRemoteDataSource
            .deleteFestivalNotification(festivalNotificationId)
            .toResult()
            .mapCatching {
                festivalNotificationLocalDataSource.deleteFestivalNotificationId(festivalId)
            }
    }

    override suspend fun syncFestivalNotificationIsAllow(): Result<Boolean> {
        val deviceId =
            deviceLocalDataSource.getDeviceId().firstOrNull() ?: return Result.failure(
                IllegalArgumentException(NO_DEVICE_ID_EXCEPTION),
            )
        val festivalId =
            festivalLocalDataSource.getFestivalId().firstOrNull() ?: return Result.failure(
                IllegalArgumentException(NO_FESTIVAL_ID_EXCEPTION),
            )

        return festivalNotificationRemoteDataSource
            .getFestivalNotification(deviceId)
            .toResult()
            .mapCatching { response ->
                val notificationId =
                    response.find { it.festivalId == festivalId }?.festivalNotificationId
                val isAllowed = notificationId != null
                festivalNotificationLocalDataSource.saveFestivalNotificationIsAllowed(
                    festivalId,
                    isAllowed,
                )
                if (isAllowed) {
                    festivalNotificationLocalDataSource.saveFestivalNotificationId(
                        festivalId,
                        notificationId,
                    )
                } else {
                    festivalNotificationLocalDataSource.deleteFestivalNotificationId(festivalId)
                }
                isAllowed
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFestivalNotificationIsAllow(): Flow<Boolean> =
        festivalLocalDataSource.getFestivalId().flatMapLatest { festivalId ->
            if (festivalId == null) {
//                    Timber.e("FestivalNotificationRepository: FestivalId가 null입니다")
                flowOf(false)
            } else {
                festivalNotificationLocalDataSource.getFestivalNotificationIsAllowed(festivalId)
            }
        }

    override suspend fun saveFestivalNotificationIsAllow(isAllowed: Boolean): Result<Unit> {
        val festivalId =
            withTimeoutOrNullFallback(
                producer = { festivalLocalDataSource.getFestivalId().firstOrNull() },
                onFallback = { /*TODO 로그 */ },
            ) ?: return Result.failure(IllegalStateException())

        return runCatching {
            festivalNotificationLocalDataSource.saveFestivalNotificationIsAllowed(
                festivalId = festivalId,
                isAllowed = isAllowed,
            )
        }
    }

    companion object {
        private val NO_FESTIVAL_ID_EXCEPTION =
            "${::FestivalNotificationRepositoryImpl.name}: FestivalId가 없습니다."
        private val NO_DEVICE_ID_EXCEPTION =
            "${::FestivalNotificationRepositoryImpl.name}: DeviceId가 없습니다."
    }
}
