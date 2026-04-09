package com.daedan.festabook.data.repository

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
    override suspend fun getMyWaiting(): Result<MyWaiting?> {
        val existResult = waitingRemoteDataSource.fetchMyWaitingExist().toResult().getOrElse { return Result.failure(it) }
        if (!existResult.exists) return Result.success(null)
        return waitingRemoteDataSource.fetchMyWaiting().toResult().mapCatching { it.toDomain() }
    }

    override suspend fun cancelWaiting(waitingId: Long): Result<Unit> = waitingRemoteDataSource.cancelWaiting(waitingId).toResult()
}
