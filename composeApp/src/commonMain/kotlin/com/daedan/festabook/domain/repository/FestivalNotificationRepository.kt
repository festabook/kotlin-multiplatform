package com.daedan.festabook.domain.repository

import kotlinx.coroutines.flow.Flow

interface FestivalNotificationRepository {
    suspend fun saveFestivalNotification(): Result<Unit>

    suspend fun deleteFestivalNotification(): Result<Unit>

    suspend fun getFestivalNotificationIsAllow(): Flow<Boolean>

    suspend fun setFestivalNotificationIsAllow(isAllowed: Boolean)
}
