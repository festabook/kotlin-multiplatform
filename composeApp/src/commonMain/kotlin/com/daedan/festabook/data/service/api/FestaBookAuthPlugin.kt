package com.daedan.festabook.data.service.api

import com.daedan.festabook.data.datasource.local.DeviceLocalDataSource
import com.daedan.festabook.data.datasource.local.FestivalLocalDataSource
import dev.zacsweers.metro.Inject
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.flow.firstOrNull

@Inject
class FestaBookAuthPlugin(
    private val festivalLocalDataSource: FestivalLocalDataSource,
    private val deviceLocalDataSource: DeviceLocalDataSource,
) {
    val plugin =
        createClientPlugin(name = "FestaBookAuthPlugin") {
            onRequest { request, _ ->
                val festivalId = festivalLocalDataSource.getFestivalId().firstOrNull() ?: return@onRequest
                request.headers["festival"] = festivalId.toString()

                val deviceId = deviceLocalDataSource.getDeviceId().firstOrNull() ?: return@onRequest
                request.headers["device"] = deviceId.toString()
            }
        }
}
