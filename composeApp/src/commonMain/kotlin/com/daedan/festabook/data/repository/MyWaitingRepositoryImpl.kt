package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.data.datasource.remote.waiting.WaitingRemoteDataSource
import com.daedan.festabook.data.model.response.waiting.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.domain.repository.MyWaitingRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.first

@ContributesBinding(AppScope::class)
@Inject
class MyWaitingRepositoryImpl(
    private val waitingRemoteDataSource: WaitingRemoteDataSource,
    private val waitingLocalDataSource: WaitingLocalDataSource,
) : MyWaitingRepository {
    override suspend fun getMyWaiting(): Result<MyWaiting?> {
        val existResult = waitingRemoteDataSource.fetchMyWaitingExist().toResult().getOrElse { return Result.failure(it) }

        if (!existResult.exists) {
            // 고아 복구: 서버에 없는데 로컬에 placeId가 남아 있으면 클리어
            if (waitingLocalDataSource.getPlaceId().first() != null) {
                waitingLocalDataSource.clearPlaceId()
            }
            return Result.success(null)
        }

        return waitingRemoteDataSource
            .fetchMyWaiting()
            .toResult()
            .mapCatching { it.toDomain() }
            .mapCatching { myWaiting ->
                // 서버 측 종료 상태 감지 → placeId 클리어 후 null 반환
                if (myWaiting.waitingStatus in
                    setOf(
                        WaitingStatus.CANCELED,
                        WaitingStatus.ARRIVED,
                        WaitingStatus.NO_SHOW,
                    )
                ) {
                    waitingLocalDataSource.clearPlaceId()
                    return@mapCatching null
                }
                val placeId = waitingLocalDataSource.getPlaceId().first()
                myWaiting.copy(placeId = placeId)
            }
    }

    override suspend fun cancelWaiting(waitingId: Long): Result<Unit> =
        waitingRemoteDataSource
            .cancelWaiting(waitingId)
            .toResult()
            .onSuccess { waitingLocalDataSource.clearPlaceId() }
}
