package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface FestivalLocalDataSource {
    suspend fun saveFestivalId(festivalId: Long)

    suspend fun getFestivalId(): Flow<Long?>

    suspend fun getIsFirstVisit(festivalId: Long): Boolean
}
