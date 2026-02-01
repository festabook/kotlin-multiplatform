package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.LineupItem
import com.daedan.festabook.domain.model.Organization
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface FestivalRepository {
    suspend fun getFestivalInfo(): Result<Organization>

    suspend fun getLineUpGroupByDate(): Result<Map<LocalDate, List<LineupItem>>>

    fun getIsFirstVisit(): Flow<Boolean>
}
