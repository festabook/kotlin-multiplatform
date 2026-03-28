package com.daedan.festabook.domain.repository

interface AppVersionRepository {
    suspend fun getLatestVersion(bundleId: String): Result<String>
}
