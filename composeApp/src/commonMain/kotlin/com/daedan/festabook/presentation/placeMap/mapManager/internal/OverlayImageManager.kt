package com.daedan.festabook.presentation.placeMap.mapManager.internal

import com.daedan.festabook.presentation.placeMap.platform.OverlayImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

/**
 * OverlayImage를 flyweight 패턴으로 관리하기 위해 필요한 객체입니다
 * 초기에 이미지 id를 받아 OverlayImage를 생성합니다
 * 이후 getImage로 저장된 OverlayImage를 반환합니다
 */
class OverlayImageManager(
    private val resources: List<DrawableResource>,
    scope: CoroutineScope,
) {
    private val images = mutableMapOf<DrawableResource, OverlayImage>()

    init {
        scope.launch {
            resources.forEach { drawable ->
                images[drawable] = OverlayImage.create(drawable)
            }
        }
    }

    fun getImage(drawable: DrawableResource): OverlayImage? = images[drawable]
}
