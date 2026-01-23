package com.daedan.festabook.data.datasource.local

interface FestivalLocalDataSource {
    suspend fun saveFestivalId(festivalId: Long)

    suspend fun getFestivalId(): Long?

    suspend fun getIsFirstVisit(): Boolean
}
