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
    private var _map: NaverMap? = null

    actual var map: NaverMap?
        get() = _map
        set(value) {
            _map = value
            platform.map = value?.platformMap
        }
}
