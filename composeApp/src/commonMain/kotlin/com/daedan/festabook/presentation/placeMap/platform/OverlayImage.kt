package com.daedan.festabook.presentation.placeMap.platform

expect class OverlayImage {
    companion object {
        fun create(icon: MarkerIcon): OverlayImage
    }
}
