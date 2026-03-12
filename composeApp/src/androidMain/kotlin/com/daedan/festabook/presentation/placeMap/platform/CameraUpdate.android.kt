package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.map.CameraAnimation as PlatformCameraAnimation
import com.naver.maps.map.CameraUpdate as PlatformCameraUpdate

actual class CameraUpdate(
    val platform: PlatformCameraUpdate,
) {
    actual fun animate(animation: CameraAnimation): CameraUpdate = CameraUpdate(platform.animate(animation.toPlatform()))

    actual companion object {
        actual fun scrollTo(position: LatLng): CameraUpdate = CameraUpdate(PlatformCameraUpdate.scrollTo(position.platform))

        actual fun zoomTo(zoom: Double): CameraUpdate = CameraUpdate(PlatformCameraUpdate.zoomTo(zoom))
    }
}

private fun CameraAnimation.toPlatform(): PlatformCameraAnimation =
    when (this) {
        CameraAnimation.None -> PlatformCameraAnimation.None
        CameraAnimation.Linear -> PlatformCameraAnimation.Linear
        CameraAnimation.Easing -> PlatformCameraAnimation.Easing
        CameraAnimation.Fly -> PlatformCameraAnimation.Fly
    }
