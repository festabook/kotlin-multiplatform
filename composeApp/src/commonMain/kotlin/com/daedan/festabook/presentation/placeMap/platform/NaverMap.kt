package com.daedan.festabook.presentation.placeMap.platform

expect class NaverMap {
    var isIndoorEnabled: Boolean
    var symbolScale: Double

    var customStyleId: String?
    val uiSettings: UiSettings
    val cameraPosition: CameraPosition

    fun moveCamera(cameraUpdate: CameraUpdate)

    fun addOnCameraChangeListener(listener: OnCameraChangeListener)

    fun setOnMapClickListener(onClick: (LatLng) -> Unit)

    fun setContentPadding(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        animate: Boolean,
    )

    fun interface OnCameraChangeListener {
        fun onCameraChange(
            reason: Int,
            animated: Boolean,
        )
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
