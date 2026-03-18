package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMGLatLng
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class LatLng actual constructor(
    latitude: Double,
    longitude: Double,
) {
    val platform = NMGLatLng.latLngWithLat(latitude, longitude)

    actual fun distanceTo(other: LatLng): Double = platform.distanceTo(other.platform)
}
