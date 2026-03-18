package com.daedan.festabook.data.datasource.remote.appversion

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.response.splash.IosAppVersionResponse
import com.daedan.festabook.data.service.AppVersionService
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class AppVersionRemoteDataSourceImpl(
    private val appVersionService: AppVersionService,
) : AppVersionRemoteDataSource {
    override suspend fun fetchIosAppVersion(): ApiResult<IosAppVersionResponse> =
        ApiResult.toApiResult { appVersionService.fetchIosAppVersion() }
}
