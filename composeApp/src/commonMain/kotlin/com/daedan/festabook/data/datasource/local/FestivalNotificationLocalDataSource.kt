package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface FestivalNotificationLocalDataSource {
    suspend fun saveFestivalNotificationId(
        festivalId: Long,
        festivalNotificationId: Long,
    )

    suspend fun getFestivalNotificationId(festivalId: Long): Flow<Long?>

    suspend fun deleteFestivalNotificationId(festivalId: Long)

    suspend fun saveFestivalNotificationIsAllowed(
        festivalId: Long,
        isAllowed: Boolean,
    )

    suspend fun getFestivalNotificationIsAllowed(festivalId: Long): Flow<Boolean>
}
