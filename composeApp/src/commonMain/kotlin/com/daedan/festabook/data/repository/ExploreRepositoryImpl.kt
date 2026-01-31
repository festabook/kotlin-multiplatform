package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.data.datasource.remote.festival.FestivalRemoteDataSource
import com.daedan.festabook.data.model.response.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.University
import com.daedan.festabook.domain.repository.ExploreRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.first

@ContributesBinding(AppScope::class)
@Inject
class ExploreRepositoryImpl(
    private val festivalRemoteDataSource: FestivalRemoteDataSource,
    private val festivalLocalDataSource: FestivalLocalDataSource,
) : ExploreRepository {
    override suspend fun search(query: String): Result<List<University>> {
//        Timber.d("Searching for query: $query")

        val response =
            festivalRemoteDataSource
                .findUniversitiesByName(universityName = query)
                .toResult()

        return response.mapCatching { universities -> universities.map { it.toDomain() } }
    }

    override suspend fun saveFestivalId(festivalId: Long) = festivalLocalDataSource.saveFestivalId(festivalId)

    override suspend fun getFestivalId(): Long? =
        festivalLocalDataSource.getFestivalId().first() ?: run {
            // 로그 달아주세요잉
            null
        }
}
