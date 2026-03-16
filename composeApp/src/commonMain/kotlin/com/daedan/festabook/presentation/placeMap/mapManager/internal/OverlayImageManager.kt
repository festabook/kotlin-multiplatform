package com.daedan.festabook.presentation.placeMap.mapManager.internal

import com.daedan.festabook.presentation.placeMap.platform.MarkerIcon
import com.daedan.festabook.presentation.placeMap.platform.OverlayImage

/**
 * OverlayImage를 flyweight 패턴으로 관리하기 위해 필요한 객체입니다
 * 초기에 이미지 id를 받아 OverlayImage를 생성합니다
 * 이후 getImage로 저장된 OverlayImage를 반환합니다
 */
class OverlayImageManager {
    private val resources = MarkerIcon.entries

    private val images = mutableMapOf<MarkerIcon, OverlayImage>()

    init {
        resources.forEach { icon ->
            images[icon] = OverlayImage.create(icon)
        }
    }

    fun getImage(icon: MarkerIcon): OverlayImage? = images[icon]
}
