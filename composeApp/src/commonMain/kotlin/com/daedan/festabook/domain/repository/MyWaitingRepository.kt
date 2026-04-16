package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.MyWaitingWithPlace

interface MyWaitingRepository {
    suspend fun getMyWaiting(): Result<MyWaitingWithPlace?>

    suspend fun cancelWaiting(waitingId: Long): Result<Unit>
}
