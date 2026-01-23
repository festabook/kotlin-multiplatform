package com.daedan.festabook

import android.app.Application
import com.daedan.festabook.di.AndroidAppGraph
import dev.zacsweers.metro.createGraphFactory

class FestabookApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }
}
