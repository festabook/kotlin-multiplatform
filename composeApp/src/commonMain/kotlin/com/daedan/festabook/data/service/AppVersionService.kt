package com.daedan.festabook.data.service

import com.daedan.festabook.data.model.response.splash.IosAppVersionResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Url

interface AppVersionService {
    @GET
    suspend fun fetchIosAppVersion(
        @Url url: String = "https://itunes.apple.com/lookup?bundleId=eoehdeksruf.festabook",
    ): Response<IosAppVersionResponse>
}
