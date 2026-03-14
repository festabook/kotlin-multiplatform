package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import cocoapods.NMapsMap.NMFLogoAlignLeftTop
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIEdgeInsetsMake

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun NaverMapLogo(
    naverMap: NaverMap?,
    modifier: Modifier,
) {
    val density = LocalDensity.current
    var targetPosition by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(targetPosition) {
        naverMap?.run {
            platformMap.mapView.apply {
                logoAlign = NMFLogoAlignLeftTop
                logoMargin =
                    UIEdgeInsetsMake(
                        top = targetPosition.y.toPt(density),
                        left = targetPosition.x.toPt(density),
                        bottom = 0.0,
                        right = 0.0,
                    )
            }
        }
    }

    Box(
        modifier =
            modifier.onGloballyPositioned { coordinates ->
                targetPosition = coordinates.positionInRoot()
            },
    )
}

private fun Float.toPt(density: Density): Double =
    with(density) {
        this@toPt
            .toDp()
            .value
            .toDouble()
    }
