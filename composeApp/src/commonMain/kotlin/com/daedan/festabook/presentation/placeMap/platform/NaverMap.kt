package com.daedan.festabook.presentation.placeMap.platform

expect class NaverMap {
    var isIndoorEnabled: Boolean
    var symbolScale: Double

    var customStyleId: String?
    val uiSettings: UiSettings

    fun setOnMapClickListener(onClick: (LatLng) -> Unit)

    fun setContentPadding(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        animate: Boolean,
    )

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
