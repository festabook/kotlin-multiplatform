package com.daedan.festabook.presentation.placeMap.platform

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import com.naver.maps.map.overlay.OverlayImage as PlatformOverlayImage

actual class OverlayImage private constructor(
    val platform: PlatformOverlayImage,
) {
    actual companion object {
        actual suspend fun create(resource: DrawableResource): OverlayImage =
            withContext(Dispatchers.IO) {
                val env = getSystemResourceEnvironment()
                val imageByteArray = getDrawableResourceBytes(env, resource)
                val imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                OverlayImage(
                    platform = PlatformOverlayImage.fromBitmap(imageBitmap),
                )
            }
    }
}
