package com.daedan.festabook.data.datasource.remote.waiting

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.MyWaitingResponse
import com.daedan.festabook.data.model.response.waiting.PlaceWaitingResponse
import com.daedan.festabook.data.model.response.waiting.WaitingExistResponse

interface WaitingRemoteDataSource {
    suspend fun fetchMyWaiting(): ApiResult<MyWaitingResponse>

    suspend fun fetchMyWaitingExist(): ApiResult<WaitingExistResponse>

    suspend fun fetchPlaceWaiting(placeId: Long): ApiResult<PlaceWaitingResponse>

    suspend fun cancelWaiting(waitingId: Long): ApiResult<Unit>

    suspend fun registerWaiting(
        placeId: Long,
        request: WaitingRegisterRequest,
    ): ApiResult<MyWaitingResponse>
}
