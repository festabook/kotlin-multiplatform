package com.daedan.festabook.data.datasource.remote.festival

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.response.FestivalSearchResponse
import com.daedan.festabook.data.model.response.festival.FestivalResponse

interface FestivalRemoteDataSource {
    suspend fun fetchFestival(): ApiResult<FestivalResponse>

    suspend fun findFestivalsByKeyword(keyword: String): ApiResult<List<FestivalSearchResponse>>
}
