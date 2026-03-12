package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.map.CameraPosition as PlatformCameraPosition

actual class CameraPosition(
    val platform: PlatformCameraPosition,
) {
    actual val target: LatLng
        get() = LatLng(platform.target.latitude, platform.target.longitude)
    actual val zoom: Double
        get() = platform.zoom
}
