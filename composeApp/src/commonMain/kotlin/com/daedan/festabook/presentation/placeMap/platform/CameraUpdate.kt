package com.daedan.festabook.presentation.placeMap.platform

expect class CameraUpdate {
    fun animate(animation: CameraAnimation): CameraUpdate

    companion object {
        fun scrollTo(position: LatLng): CameraUpdate

        fun zoomTo(zoom: Double): CameraUpdate
    }
}

enum class CameraAnimation {
    None,
    Linear,
    Easing,
    Fly,
}
