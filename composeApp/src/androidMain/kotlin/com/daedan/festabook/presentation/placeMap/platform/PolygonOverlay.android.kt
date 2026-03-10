package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.map.overlay.PolygonOverlay as PlatformPolygonOverlay

actual class PolygonOverlay actual constructor(
    coords: List<LatLng>,
    holes: List<List<LatLng>>,
) {
    val platform =
        PlatformPolygonOverlay().apply {
            this.coords = coords.map { it.platform }
            this.holes =
                holes.map { latLngs ->
                    latLngs.map { it.platform }
                }
        }

    private var commonMap: NaverMap? = null

    actual var color: Int
        get() = platform.color
        set(value) {
            platform.color = value
        }
    actual var outlineWidth: Int
        get() = platform.outlineWidth
        set(value) {
            platform.outlineWidth = value
        }
    actual var map: NaverMap?
        get() = commonMap
        set(value) {
            commonMap = value
            platform.map = value?.platformMap
        }
}
