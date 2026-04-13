package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.model.request.WaitingRegisterRequest
import com.daedan.festabook.data.model.response.waiting.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.PlaceWaiting
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class WaitingRegisterInfoRepositoryImpl(
    private val waitingRemoteDataSource: WaitingRemoteDataSource,
) : WaitingRegisterInfoRepository {
    override suspend fun getPlaceWaiting(placeId: Long): Result<PlaceWaiting> {
        val response = waitingRemoteDataSource.fetchPlaceWaiting(placeId).toResult()
        return response.mapCatching { it.toDomain() }
    }

    override suspend fun registerWaiting(
        placeId: Long,
        partySize: Int,
    ): Result<MyWaiting> {
        val request = WaitingRegisterRequest(partySize = partySize)
        val response = waitingRemoteDataSource.registerWaiting(placeId, request).toResult()
        return response.mapCatching { it.toDomain() }
    }
}
