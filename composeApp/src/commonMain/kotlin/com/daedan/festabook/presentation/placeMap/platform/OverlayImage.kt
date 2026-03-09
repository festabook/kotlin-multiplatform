package com.daedan.festabook.presentation.placeMap.platform

import org.jetbrains.compose.resources.DrawableResource

expect class OverlayImage {
    companion object {
        suspend fun create(resource: DrawableResource): OverlayImage
    }
}
