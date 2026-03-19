package com.daedan.festabook.data.datasource.remote.appversion

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.model.response.splash.IosAppVersionResponse

interface AppVersionRemoteDataSource {
    suspend fun fetchIosAppVersion(bundleId: String): ApiResult<IosAppVersionResponse>
}
