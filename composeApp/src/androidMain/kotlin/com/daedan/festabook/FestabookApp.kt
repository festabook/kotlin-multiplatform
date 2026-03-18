package com.daedan.festabook

import android.app.Application
import com.daedan.festabook.di.AndroidAppGraph
import com.naver.maps.map.NaverMapSdk
import dev.zacsweers.metro.createGraphFactory

class FestabookApp : Application() {
    val festabookAppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        setupNaverSdk()
    }

    private fun setupNaverSdk() {
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NcpKeyClient(BuildKonfig.NAVER_MAP_CLIENT_ID)
    }
}
