package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFCameraPosition
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class CameraPosition(
    val platform: NMFCameraPosition,
) {
    actual val target: LatLng
        get() = LatLng(platform.target.lat(), platform.target.lng())
    actual val zoom: Double
        get() = platform.zoom
}
