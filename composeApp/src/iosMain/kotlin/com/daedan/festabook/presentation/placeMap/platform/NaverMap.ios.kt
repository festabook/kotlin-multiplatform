package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFNaverMapView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue

@OptIn(ExperimentalForeignApi::class)
actual class NaverMap(
    val platformMap: NMFNaverMapView,
) {
    actual var isIndoorEnabled: Boolean
        get() = platformMap.mapView.isIndoorMapEnabled()
        set(value) {
            platformMap.mapView.setIndoorMapEnabled(value)
        }
    actual var symbolScale: Double
        get() = platformMap.mapView.symbolScale()
        set(value) {
            platformMap.mapView.setSymbolScale(value)
        }
    actual var customStyleId: String?
        get() = platformMap.mapView.customStyleId
        set(value) {
            platformMap.mapView.setCustomStyleId(value)
        }
    actual val uiSettings: UiSettings = UiSettings(platformMap)

    actual fun setOnMapClickListener(onClick: (LatLng) -> Unit) {
    }

    actual fun setContentPadding(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        animate: Boolean,
    ) {
    }

    actual class UiSettings(
        private val map: NMFNaverMapView,
    ) {
        actual var isZoomControlEnabled: Boolean
            get() = map.mapView.isZoomGestureEnabled()
            set(value) {
                map.mapView.setZoomGestureEnabled(value)
            }
        actual var isScaleBarEnabled: Boolean
            get() = map.showScaleBar
            set(value) {
                map.showScaleBar = value
            }

        actual fun setLogoMargin(
            start: Int,
            top: Int,
            end: Int,
            bottom: Int,
        ) {
            map.mapView.setLogoMargin(
                cValue {
                    this.left = start.toDouble()
                    this.top = top.toDouble()
                    this.right = end.toDouble()
                    this.bottom = bottom.toDouble()
                },
            )
        }
    }
}
