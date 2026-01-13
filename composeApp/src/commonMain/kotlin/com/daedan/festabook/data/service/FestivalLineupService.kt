package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.lineup.LineupResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET

interface FestivalLineupService {
    @GET("lineups")
    suspend fun fetchLineup(): Response<List<LineupResponse>>
}
