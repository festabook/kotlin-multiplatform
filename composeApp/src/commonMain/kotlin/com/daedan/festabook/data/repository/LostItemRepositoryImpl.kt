package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.lostitem.LostItemDataSource
import com.daedan.festabook.data.model.response.lostitem.toDomain
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.model.Lost
import com.daedan.festabook.domain.model.LostItemStatus
import com.daedan.festabook.domain.repository.LostItemRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class LostItemRepositoryImpl(
    private val lostItemDataSource: LostItemDataSource,
) : LostItemRepository {
    override suspend fun getPendingLostItems(): Result<List<Lost>> =
        lostItemDataSource
            .fetchAllLostItems()
            .toResult()
            .mapCatching { lostItemResponses ->
                lostItemResponses
                    .map { lostItemResponse -> lostItemResponse.toDomain() }
                    .filter { it.status == LostItemStatus.PENDING }
                    .sortedByDescending { it.createdAt }
            }

    override suspend fun getLostGuideItem(): Result<Lost> =
        lostItemDataSource.fetchLostGuideItem().toResult().mapCatching {
            it.toDomain()
        }

    override suspend fun getLost(): Result<List<Lost?>> =
        coroutineScope {
            val guide = async { getLostGuideItem() }
            val pendingItems = async { getPendingLostItems() }

            val guideResult = guide.await().getOrElse { return@coroutineScope Result.failure(it) }
            val pendingItemsResult =
                pendingItems.await().getOrElse { return@coroutineScope Result.failure(it) }

            Result.success(
                buildList {
                    add(guideResult)
                    addAll(pendingItemsResult)
                },
            )
        }
}
