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
//                val festivalId = festivalLocalDataSource.getFestivalId()
//                헤더가 필요한데 현재 탐색화면에서 id를 고를 수가 없어서 임시로 하드 코딩 해놨습니다!!!
                val festivalId = 1

                request.headers["festival"] = festivalId.toString()
            }
        }
}
