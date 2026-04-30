package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.data.datasource.remote.festival.FestivalRemoteDataSource
import com.daedan.festabook.data.datasource.remote.lineup.LineupDataSource
import com.daedan.festabook.data.model.response.festival.toDomain
import com.daedan.festabook.data.model.response.lineup.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.Festating
import com.daedan.festabook.domain.model.LineupItem
import com.daedan.festabook.domain.model.Organization
import com.daedan.festabook.domain.repository.FestivalRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class FestivalRepositoryImpl(
    private val festivalRemoteDataSource: FestivalRemoteDataSource,
    private val festivalLocalDataSource: FestivalLocalDataSource,
    private val lineupDataSource: LineupDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
) : FestivalRepository {
    private var cachedOrganization: Organization? = null

    override suspend fun getFestivalInfo(): Result<Organization> {
        val response = festivalRemoteDataSource.fetchFestival().toResult()
        return response.mapCatching { response ->
            response.toDomain().also { cachedOrganization = it }
        }
    }

    override suspend fun getLineUpGroupByDate(): Result<Map<LocalDate, List<LineupItem>>> {
        val response = lineupDataSource.fetchLineup().toResult()
        return response.mapCatching { lineupResponses ->
            lineupResponses
                .map { it.toDomain() }
                .groupBy { it.performanceAt.date }
        }
    }

    override fun getIsFirstVisit(): Flow<Boolean> = festivalLocalDataSource.getIsFirstVisit()

    override suspend fun getFestating(): Result<Festating> =
        runCatching {
            val organization = cachedOrganization ?: getFestivalInfo().getOrThrow()

            val deviceId =
                deviceLocalDataSource.getDeviceId().firstOrNull()
                    ?: throw IllegalStateException("deviceId 없음")

            Festating(
                organizationId = organization.id,
                festivalId = organization.festival.id,
                deviceId = deviceId,
            )
        }
}
