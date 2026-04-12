package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import com.daedan.festabook.data.datasource.remote.festival.FestivalRemoteDataSource
import com.daedan.festabook.data.model.entity.toDomain
import com.daedan.festabook.data.model.entity.toEntity
import com.daedan.festabook.data.model.response.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.data.util.withTimeoutOrNullFallback
import com.daedan.festabook.domain.model.FestivalSearchItem
import com.daedan.festabook.domain.repository.ExploreRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
class ExploreRepositoryImpl(
    private val festivalRemoteDataSource: FestivalRemoteDataSource,
    private val festivalLocalDataSource: FestivalLocalDataSource,
) : ExploreRepository {
    override suspend fun search(query: String): Result<List<FestivalSearchItem>> {
//        Timber.d("Searching for query: $query")

        val response =
            festivalRemoteDataSource
                .findFestivalsByKeyword(keyword = query)
                .toResult()

        return response.mapCatching { searchItem -> searchItem.map { it.toDomain() } }
    }

    override suspend fun saveFestivalId(festivalId: Long) = festivalLocalDataSource.saveFestivalId(festivalId)

    override suspend fun getFestivalId(): Long? =
        withTimeoutOrNullFallback(
            producer = { festivalLocalDataSource.getFestivalId().firstOrNull() },
            onFallback = { /*TODO 로그 */ },
        )

    override suspend fun saveRecentFestivalSearch(festivalSearchItem: FestivalSearchItem) =
        festivalLocalDataSource.saveRecentFestivalSearch(festivalSearchItem.toEntity())

    override fun getRecentFestivalSearches(): Flow<List<FestivalSearchItem>> =
        festivalLocalDataSource.getRecentFestivalSearches().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun clearRecentFestivalSearches() = festivalLocalDataSource.clearRecentFestivalSearches()

    override suspend fun deleteRecentFestivalSearch(festivalSearchItem: FestivalSearchItem) =
        festivalLocalDataSource.deleteRecentFestivalSearch(festivalSearchItem.toEntity())
}
