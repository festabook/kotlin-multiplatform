package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface FcmDataSource {
    suspend fun saveFcmToken(token: String)

    fun getFcmToken(): Flow<String?>
}
