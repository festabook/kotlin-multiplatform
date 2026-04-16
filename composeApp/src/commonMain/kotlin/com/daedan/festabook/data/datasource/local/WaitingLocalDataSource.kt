package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface WaitingLocalDataSource {
    suspend fun savePhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): Flow<String?>

    suspend fun savePlaceId(placeId: Long)

    fun getPlaceId(): Flow<Long?>

    suspend fun clearPlaceId()
}
