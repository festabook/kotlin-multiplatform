package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.geometry.LatLng as NaverLatLng

actual class LatLng actual constructor(
    latitude: Double,
    longitude: Double,
) {
    val platform = NaverLatLng(latitude, longitude)
}
