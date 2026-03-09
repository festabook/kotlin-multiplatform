package com.daedan.festabook.presentation.placeMap.platform

expect class PolygonOverlay(
    coords: List<LatLng>,
    holes: List<List<LatLng>>,
) {
    var color: Int
    var outlineWidth: Int
    var map: NaverMap?
}
