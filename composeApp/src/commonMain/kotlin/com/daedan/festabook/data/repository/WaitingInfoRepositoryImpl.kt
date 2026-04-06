package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.WaitingLocalDataSource
import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class WaitingInfoRepositoryImpl(
    private val waitingLocalDataSource: WaitingLocalDataSource,
) : WaitingInfoRepository {

    override suspend fun getWaitingInfo(): Result<WaitingInfo?> =
        runCatching {
            waitingLocalDataSource.getPhoneNumber().firstOrNull()
                ?.let { WaitingInfo(phoneNumber = it) }
        }

    override suspend fun saveWaitingInfo(waitingInfo: WaitingInfo): Result<Unit> =
        runCatching {
            waitingLocalDataSource.savePhoneNumber(waitingInfo.phoneNumber)
        }
}
