package com.daedan.festabook

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.daedan.festabook.di.createIosAppGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

private val festabookAppGraph = createIosAppGraph()

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        val metroVmf = festabookAppGraph.metroViewModelFactory
        CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
            App()
        }
    }
