package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFCameraUpdate
import cocoapods.NMapsMap.NMFCameraUpdateAnimation
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class CameraUpdate(
    val platform: NMFCameraUpdate,
) {
    actual fun animate(animation: CameraAnimation): CameraUpdate {
        platform.animation = animation.toPlatform()
        return this
    }

    actual companion object {
        actual fun scrollTo(position: LatLng): CameraUpdate = CameraUpdate(NMFCameraUpdate.cameraUpdateWithScrollTo(position.platform))

        actual fun zoomTo(zoom: Double): CameraUpdate = CameraUpdate(NMFCameraUpdate.cameraUpdateWithZoomTo(zoom))
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun CameraAnimation.toPlatform(): NMFCameraUpdateAnimation =
    when (this) {
        CameraAnimation.None -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationNone
        CameraAnimation.Linear -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationLinear
        CameraAnimation.Easing -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationEaseOut
        CameraAnimation.Fly -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationFly
    }
