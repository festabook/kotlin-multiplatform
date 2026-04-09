package com.daedan.festabook.data.datasource.remote.waiting

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.PlaceWaitingResponse
import com.daedan.festabook.data.model.response.waiting.WaitingExistResponse
import com.daedan.festabook.data.service.PlaceService
import com.daedan.festabook.data.service.WaitingService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class WaitingRemoteDataSourceImpl(
    private val waitingService: WaitingService,
    private val placeService: PlaceService,
) : WaitingRemoteDataSource {
    override suspend fun fetchMyWaiting(): ApiResult<MyWaitingResponse> = ApiResult.toApiResult { waitingService.fetchMyWaiting() }

    override suspend fun fetchMyWaitingExist(): ApiResult<WaitingExistResponse> =
        ApiResult.toApiResult { waitingService.fetchMyWaitingExist() }

    override suspend fun fetchPlaceWaiting(placeId: Long): ApiResult<PlaceWaitingResponse> =
        ApiResult.toApiResult { placeService.fetchPlaceWaiting(placeId) }

    override suspend fun cancelWaiting(waitingId: Long): ApiResult<Unit> = ApiResult.toApiResult { waitingService.cancelWaiting(waitingId) }

    override suspend fun registerWaiting(
        placeId: Long,
        request: WaitingRegisterRequest,
    ): ApiResult<MyWaitingResponse> = ApiResult.toApiResult { placeService.registerWaiting(placeId, request) }
}
