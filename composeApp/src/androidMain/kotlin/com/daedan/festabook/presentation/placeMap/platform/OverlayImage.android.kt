package com.daedan.festabook.presentation.placeMap.platform

import com.naver.maps.map.overlay.OverlayImage as PlatformOverlayImage

actual class OverlayImage private constructor(
    val platform: PlatformOverlayImage,
) {
    actual companion object {
        actual fun create(icon: MarkerIcon): OverlayImage =
            OverlayImage(
                platform = PlatformOverlayImage.fromResource(icon.toResId()),
            )
    }
}
