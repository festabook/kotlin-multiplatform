package com.daedan.festabook.presentation.placeMap.platform

expect class Marker() {
    var tag: Any?
    var isForceShowCaption: Boolean
    var zIndex: Int
    var captionMinZoom: Double
    var icon: OverlayImage
    var width: Int
    var height: Int
    var position: LatLng
    var map: NaverMap?
    var captionText: String
    var isHideCollidedCaptions: Boolean
    var isVisible: Boolean

    fun setOnClickListener(listener: () -> Boolean)

    companion object {
        val SIZE_AUTO: Int
    }
}
