package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.geometry.LatLng as PlatformLatLng

actual class LatLng actual constructor(
    latitude: Double,
    longitude: Double,
) {
    val platform = PlatformLatLng(latitude, longitude)

    actual fun distanceTo(other: LatLng): Double = platform.distanceTo(other.platform)
}
