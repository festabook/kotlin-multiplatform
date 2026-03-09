package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFOverlayImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual class OverlayImage private constructor(
    val platform: NMFOverlayImage,
) {
    actual companion object {
        actual suspend fun create(resource: DrawableResource): OverlayImage =
            withContext(Dispatchers.IO) {
                val env = getSystemResourceEnvironment()
                val imageByteArray = getDrawableResourceBytes(env, resource)
                val imageNSData =
                    imageByteArray.usePinned { pinned ->
                        NSData.dataWithBytes(
                            bytes = pinned.addressOf(0),
                            length = imageByteArray.size.toULong(),
                        )
                    }
                val uiImage = UIImage(imageNSData)
                OverlayImage(
                    platform = NMFOverlayImage.overlayImageWithImage(uiImage),
                )
            }
    }
}
