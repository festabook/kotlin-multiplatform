package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.model.response.waiting.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.repository.MyWaitingRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class MyWaitingRepositoryImpl(
    private val waitingRemoteDataSource: WaitingRemoteDataSource,
) : MyWaitingRepository {

    override suspend fun getMyWaiting(festivalId: Long): Result<MyWaiting?> {
        return when (val apiResult = waitingRemoteDataSource.fetchMyWaiting(festivalId)) {
            is ApiResult.ClientError -> if (apiResult.code == 404) Result.success(null)
                else apiResult.toResult()
            else -> apiResult.toResult().mapCatching { it.toDomain() }
        }
    }

    override suspend fun cancelWaiting(waitingId: Long): Result<Unit> =
        waitingRemoteDataSource.cancelWaiting(waitingId).toResult()
}
