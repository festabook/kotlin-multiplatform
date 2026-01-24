package com.daedan.festabook.data.datasource.local

interface FcmDataSource {
    suspend fun saveFcmToken(token: String)

    suspend fun getFcmToken(): String?
}
