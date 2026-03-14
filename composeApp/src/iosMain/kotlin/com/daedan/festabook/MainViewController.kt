package com.daedan.festabook

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daedan.festabook.di.IosAppGraph
import com.daedan.festabook.presentation.placeMap.component.PlaceMapRoute
import com.daedan.festabook.presentation.platform.rememberLocationSource
import com.daedan.festabook.presentation.theme.FestabookTheme
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

private val festabookAppGraph = createGraph<IosAppGraph>()
private val metroVmf = festabookAppGraph.metroViewModelFactory

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        FestabookTheme {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                PlaceMapRoute(
                    placeMapViewModel = viewModel(),
                    onStartPlaceDetail = {},
                    onShowErrorSnackBar = {},
                    locationSource = rememberLocationSource(),
                )
            }
        }
    }
