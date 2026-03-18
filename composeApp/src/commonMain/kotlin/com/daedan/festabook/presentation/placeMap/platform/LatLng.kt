package com.daedan.festabook.presentation.placeMap.platform

expect class LatLng(
    latitude: Double,
    longitude: Double,
) {
    fun distanceTo(other: LatLng): Double
}
