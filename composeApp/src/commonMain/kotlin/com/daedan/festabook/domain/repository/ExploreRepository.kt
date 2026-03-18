package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.FestivalSearchItem

interface ExploreRepository {
    suspend fun search(query: String): Result<List<FestivalSearchItem>>

    suspend fun saveFestivalId(festivalId: Long)

    suspend fun getFestivalId(): Long?
}
