package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.WaitingInfo

interface WaitingInfoRepository {
    suspend fun getWaitingInfo(): Result<WaitingInfo?>

    suspend fun saveWaitingInfo(waitingInfo: WaitingInfo): Result<Unit>
}
