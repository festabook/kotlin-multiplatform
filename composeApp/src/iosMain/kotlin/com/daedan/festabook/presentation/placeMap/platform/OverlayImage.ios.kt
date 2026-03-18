package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFOverlayImage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual class OverlayImage private constructor(
    val platform: NMFOverlayImage,
) {
    actual companion object {
        actual fun create(icon: MarkerIcon): OverlayImage {
            val uiImage = UIImage.imageNamed(icon.toName())
            return OverlayImage(
                platform = NMFOverlayImage.overlayImageWithImage(uiImage!!),
            )
        }
    }
}
