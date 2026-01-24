package com.daedan.festabook.domain.repository

interface FestivalNotificationRepository {
    suspend fun saveFestivalNotification(): Result<Unit>

    suspend fun deleteFestivalNotification(): Result<Unit>

    suspend fun getFestivalNotificationIsAllow(): Boolean

    suspend fun setFestivalNotificationIsAllow(isAllowed: Boolean)
}
