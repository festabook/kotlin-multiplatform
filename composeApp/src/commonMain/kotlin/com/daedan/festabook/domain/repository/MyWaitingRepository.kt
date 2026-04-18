package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.MyWaiting

interface MyWaitingRepository {
    suspend fun getMyWaiting(): Result<MyWaiting?>

    suspend fun cancelWaiting(waitingId: Long): Result<Unit>
}
