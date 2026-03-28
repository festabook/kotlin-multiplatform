package com.daedan.festabook.presentation.placeMap.platform

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

expect class NaverMap {
    var isIndoorEnabled: Boolean
    var symbolScale: Double

    var customStyleId: String?
    val uiSettings: UiSettings
    val cameraPosition: CameraPosition

    var locationSource: LocationSource?

    fun moveCamera(cameraUpdate: CameraUpdate)

    fun addOnCameraChangeListener(listener: OnCameraChangeListener)

    fun addOnLocationChangeListener(listener: OnLocationChangeListener)

    fun setOnMapClickListener(onClick: (LatLng) -> Unit)

    fun setContentPadding(
        left: Dp,
        top: Dp,
        right: Dp,
        bottom: Dp,
        density: Density,
        animate: Boolean,
    )

    fun interface OnCameraChangeListener {
        fun onCameraChange(
            reason: Int,
            animated: Boolean,
        )
    }

    fun interface OnLocationChangeListener {
        fun onLocationChange()
    }

    class UiSettings {
        var isZoomControlEnabled: Boolean
        var isScaleBarEnabled: Boolean

        fun setLogoMargin(
            start: Int,
            top: Int,
            end: Int,
            bottom: Int,
        )
    }
}
