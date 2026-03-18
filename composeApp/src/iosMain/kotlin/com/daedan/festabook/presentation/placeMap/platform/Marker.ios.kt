package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFMarker
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class Marker {
    val platform = NMFMarker()

    private var _tag: Any? = null
    actual var tag: Any?
        get() = _tag
        set(value) {
            _tag = value
        }

    actual var isForceShowCaption: Boolean
        get() = platform.isForceShowCaption
        set(value) {
            platform.isForceShowCaption = value
        }

    actual var zIndex: Int
        get() = platform.zIndex.toInt()
        set(value) {
            platform.zIndex = value.toLong()
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
            platform.iconImage = value.platform
        }

    actual var width: Int
        get() = platform.width.toInt()
        set(value) {
            platform.width = if (value == SIZE_AUTO) 0.0 else value.toDouble()
        }

    actual var height: Int
        get() = platform.height.toInt()
        set(value) {
            platform.height = if (value == SIZE_AUTO) 0.0 else value.toDouble()
        }

    actual var position: LatLng
        get() = LatLng(platform.position.lat(), platform.position.lng())
        set(value) {
            platform.position = value.platform
        }

    private var _map: NaverMap? = null
    actual var map: NaverMap?
        get() = _map
        set(value) {
            _map = value
            platform.mapView = value?.platformMap?.mapView
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
        get() = !platform.hidden
        set(value) {
            platform.hidden = !value
        }

    actual fun setOnClickListener(listener: () -> Boolean) {
        platform.touchHandler = { _ -> listener() }
    }

    actual companion object {
        actual val SIZE_AUTO: Int = -1
    }
}
