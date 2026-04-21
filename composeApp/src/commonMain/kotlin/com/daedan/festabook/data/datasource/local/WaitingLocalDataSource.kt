package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface WaitingLocalDataSource {
    suspend fun savePhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): Flow<String?>

    // TODO: 서버에서 placeId 내려주면 삭제

    suspend fun savePlaceId(placeId: Long)

    // TODO: 서버에서 placeId 내려주면 삭제
    fun getPlaceId(): Flow<Long?>

    // TODO: 서버에서 placeId 내려주면 삭제

    suspend fun clearPlaceId()
}
