package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.NMapsMap.NMFLocationButton
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CurrentLocationButton(
    modifier: Modifier,
    map: NaverMap?,
    visible: Boolean,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            NMFLocationButton()
        },
        update = { button ->
            button.mapView = map?.platformMap?.mapView
            button.alpha = if (visible) 1.0 else 0.0
        },
        onRelease = { view -> view.removeFromSuperview() },
    )
}
