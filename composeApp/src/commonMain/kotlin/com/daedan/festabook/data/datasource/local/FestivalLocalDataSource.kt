package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface FestivalLocalDataSource {
    suspend fun saveFestivalId(festivalId: Long)

    fun getFestivalId(): Flow<Long?>

    fun getIsFirstVisit(): Flow<Boolean>
}
