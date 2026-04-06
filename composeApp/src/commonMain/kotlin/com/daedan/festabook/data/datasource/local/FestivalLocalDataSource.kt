package com.daedan.festabook.data.datasource.local

import com.daedan.festabook.data.model.entity.FestivalSearchItemEntity
import kotlinx.coroutines.flow.Flow

interface FestivalLocalDataSource {
    suspend fun saveFestivalId(festivalId: Long)

    fun getFestivalId(): Flow<Long?>

    suspend fun saveRecentFestivalSearch(festivalSearchItemEntity: FestivalSearchItemEntity)

    fun getRecentFestivalSearches(): Flow<List<FestivalSearchItemEntity>>

    fun getIsFirstVisit(): Flow<Boolean>
}
