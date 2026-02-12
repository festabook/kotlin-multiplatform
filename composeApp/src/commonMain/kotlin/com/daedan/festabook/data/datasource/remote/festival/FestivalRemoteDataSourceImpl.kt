package com.daedan.festabook.data.datasource.remote.festival

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.response.FestivalSearchResponse
import com.daedan.festabook.data.model.response.festival.FestivalResponse
import com.daedan.festabook.data.service.FestivalService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class FestivalRemoteDataSourceImpl(
    private val festivalService: FestivalService,
) : FestivalRemoteDataSource {
    override suspend fun fetchFestival(): ApiResult<FestivalResponse> =
        ApiResult.toApiResult {
            festivalService.fetchOrganization()
        }

    override suspend fun findFestivalsByKeyword(keyword: String): ApiResult<List<FestivalSearchResponse>> =
        ApiResult.toApiResult {
            festivalService.findFestivalsByKeyword(
                keyword = keyword,
            )
        }
}
