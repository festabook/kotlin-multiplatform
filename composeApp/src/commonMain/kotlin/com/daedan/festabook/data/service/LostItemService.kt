package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.lostitem.LostItemResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET

interface LostItemService {
    @GET("lost-items")
    suspend fun fetchAllLostItems(): Response<List<LostItemResponse>>
}
