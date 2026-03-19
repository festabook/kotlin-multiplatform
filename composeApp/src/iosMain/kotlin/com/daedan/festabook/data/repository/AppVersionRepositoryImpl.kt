package com.daedan.festabook.data.repository

import com.daedan.festabook.data.datasource.remote.appversion.AppVersionRemoteDataSource
import com.daedan.festabook.data.util.toResult
import com.daedan.festabook.domain.repository.AppVersionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class AppVersionRepositoryImpl(
    private val appVersionRemoteDataSource: AppVersionRemoteDataSource,
) : AppVersionRepository {
    override suspend fun getLatestVersion(bundleId: String): Result<String> =
        appVersionRemoteDataSource
            .fetchIosAppVersion(bundleId)
            .toResult()
            .mapCatching { response ->
                response.results.firstOrNull()?.version ?: error("App Store 조회 결과가 없습니다.")
            }
}
