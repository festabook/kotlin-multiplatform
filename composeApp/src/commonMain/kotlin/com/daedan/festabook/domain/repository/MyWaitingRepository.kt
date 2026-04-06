package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.MyWaiting

interface MyWaitingRepository {
    suspend fun getMyWaiting(festivalId: Long): Result<MyWaiting?>
    suspend fun cancelWaiting(waitingId: Long): Result<Unit>
}
