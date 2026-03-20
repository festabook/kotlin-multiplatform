package com.daedan.festabook

import android.app.Application
import com.daedan.festabook.di.AndroidAppGraph
import dev.zacsweers.metro.createGraphFactory

class FestabookApp : Application() {
    val androidAppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }
}
