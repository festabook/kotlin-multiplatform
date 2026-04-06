package com.daedan.festabook.domain.repository

import com.daedan.festabook.domain.model.PlaceWaiting
import com.daedan.festabook.domain.model.MyWaiting

interface WaitingRegisterInfoRepository {
    suspend fun getPlaceWaiting(placeId: Long): Result<PlaceWaiting>
    suspend fun registerWaiting(placeId: Long, headCount: Int, phoneNumber: String): Result<MyWaiting>
}
