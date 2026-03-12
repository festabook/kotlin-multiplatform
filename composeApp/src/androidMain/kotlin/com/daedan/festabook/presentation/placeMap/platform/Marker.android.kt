package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.map.overlay.Marker as PlatformMarker

actual class Marker {
    val platform = PlatformMarker()

    actual var tag: Any?
        get() = platform.tag
        set(value) {
            platform.tag = value
        }

    actual var isForceShowCaption: Boolean
        get() = platform.isForceShowCaption
        set(value) {
            platform.isForceShowCaption = value
        }

    actual var zIndex: Int
        get() = platform.zIndex
        set(value) {
            platform.zIndex = value
        }

    actual var captionMinZoom: Double
        get() = platform.captionMinZoom
        set(value) {
            platform.captionMinZoom = value
        }

    private var _icon: OverlayImage? = null
    actual var icon: OverlayImage
        get() = _icon!!
        set(value) {
            _icon = value
            platform.icon = value.platform
        }

    actual var width: Int
        get() = platform.width
        set(value) {
            platform.width = value
        }

    actual var height: Int
        get() = platform.height
        set(value) {
            platform.height = value
        }

    actual var position: LatLng
        get() = LatLng(platform.position.latitude, platform.position.longitude)
        set(value) {
            platform.position = value.platform
        }

    private var _map: NaverMap? = null
    actual var map: NaverMap?
        get() = _map
        set(value) {
            _map = value
            platform.map = value?.platformMap
        }

    actual var captionText: String
        get() = platform.captionText
        set(value) {
            platform.captionText = value
        }

    actual var isHideCollidedCaptions: Boolean
        get() = platform.isHideCollidedCaptions
        set(value) {
            platform.isHideCollidedCaptions = value
        }

    actual var isVisible: Boolean
        get() = platform.isVisible
        set(value) {
            platform.isVisible = value
        }

    actual fun setOnClickListener(listener: () -> Boolean) {
        platform.setOnClickListener { listener() }
    }

    actual companion object {
        actual val SIZE_AUTO: Int = PlatformMarker.SIZE_AUTO
    }
}
