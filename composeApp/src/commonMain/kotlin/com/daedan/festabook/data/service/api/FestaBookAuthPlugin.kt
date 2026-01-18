package com.daedan.festabook.data.service.api

import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import dev.zacsweers.metro.Inject
import io.ktor.client.plugins.api.createClientPlugin

@Inject
class FestaBookAuthPlugin(
    private val festivalLocalDataSource: FestivalLocalDataSource,
) {
    val plugin =
        createClientPlugin(name = "FestaBookAuthPlugin") {
            onRequest { request, _ ->
                val festivalId = festivalLocalDataSource.getFestivalId()

                if (festivalId != null) {
//                Timber.d("festivalId : $festivalId")
                    request.headers["festival"] = festivalId.toString()
                }
            }
        }
}
