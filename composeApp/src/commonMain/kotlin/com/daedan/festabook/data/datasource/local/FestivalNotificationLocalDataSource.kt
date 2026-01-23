package com.daedan.festabook.data.datasource.local

interface FestivalNotificationLocalDataSource {
    suspend fun saveFestivalNotificationId(
        festivalId: Long,
        festivalNotificationId: Long,
    )

    suspend fun getFestivalNotificationId(festivalId: Long): Long

    suspend fun deleteFestivalNotificationId(festivalId: Long)

    suspend fun clearAll()

    suspend fun saveFestivalNotificationIsAllowed(
        festivalId: Long,
        isAllowed: Boolean,
    )

    suspend fun getFestivalNotificationIsAllowed(festivalId: Long): Boolean
}
