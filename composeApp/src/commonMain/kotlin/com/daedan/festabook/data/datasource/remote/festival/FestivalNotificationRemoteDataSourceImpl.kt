package com.daedan.festabook.data.datasource.remote.festival

import com.daedan.festabook.Platform
import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.request.FestivalNotificationRequest
import com.daedan.festabook.data.model.response.festival.FestivalNotificationResponse
import com.daedan.festabook.data.model.response.festival.RegisteredFestivalNotificationResponse
import com.daedan.festabook.data.service.FestivalNotificationService
import com.daedan.festabook.getPlatform
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class FestivalNotificationRemoteDataSourceImpl(
    private val festivalNotificationService: FestivalNotificationService,
) : FestivalNotificationRemoteDataSource {
    override suspend fun saveFestivalNotification(
        festivalId: Long,
        deviceId: Long,
    ): ApiResult<FestivalNotificationResponse> =
        ApiResult.toApiResult {
            festivalNotificationService.saveFestivalNotification(
                festivalId,
                platform,
                FestivalNotificationRequest(deviceId = deviceId),
            )
        }

    override suspend fun deleteFestivalNotification(festivalNotificationId: Long): ApiResult<Unit> =
        ApiResult.toApiResult {
            festivalNotificationService.deleteFestivalNotification(festivalNotificationId)
        }

    override suspend fun getFestivalNotification(deviceId: Long): ApiResult<List<RegisteredFestivalNotificationResponse>> =
        ApiResult.toApiResult {
            festivalNotificationService.getFestivalNotification(deviceId)
        }

    companion object {
        private val platform =
            when (getPlatform()) {
                Platform.ANDROID -> "android"
                Platform.IOS -> "ios"
            }
    }
}
