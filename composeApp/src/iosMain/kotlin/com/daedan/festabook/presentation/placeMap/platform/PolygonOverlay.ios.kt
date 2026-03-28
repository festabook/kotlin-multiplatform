package com.daedan.festabook.presentation.placeMap.platform

import cocoapods.NMapsMap.NMFPolygonOverlay
import cocoapods.NMapsMap.NMGLineString
import cocoapods.NMapsMap.NMGPolygon
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloatVar
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
actual class PolygonOverlay actual constructor(
    coords: List<LatLng>,
    holes: List<List<LatLng>>,
) {
    private var commonMap: NaverMap? = null

    private val polygon =
        NMGPolygon.polygonWithRing(
            exteriorRing = NMGLineString(coords.map { it.platform }),
            interiorRings =
                holes.map { hole ->
                    NMGLineString(hole.map { it.platform })
                },
        )
    val platform = NMFPolygonOverlay.polygonOverlay(polygon)

    actual var color: Int
        get() = platform!!.fillColor.toHexInt()
        set(value) {
            platform!!.fillColor = value.toUIColor()
        }
    actual var outlineWidth: Int
        get() = platform!!.outlineWidth.toInt()
        set(value) {
            platform!!.outlineWidth = (value / 2).toULong()
        }
    actual var map: NaverMap?
        get() = commonMap
        set(value) {
            commonMap = value
            platform!!.mapView = value?.platformMap?.mapView
        }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIColor.toHexInt(): Int {
    return memScoped {
        val r = alloc<CGFloatVar>()
        val g = alloc<CGFloatVar>()
        val b = alloc<CGFloatVar>()
        val a = alloc<CGFloatVar>()

        val success =
            this@toHexInt.getRed(
                red = r.ptr,
                green = g.ptr,
                blue = b.ptr,
                alpha = a.ptr,
            )

        if (!success) return 0 // 실패 시 기본값 (검정색 등)

        val red = (r.value * 255).toInt()
        val green = (g.value * 255).toInt()
        val blue = (b.value * 255).toInt()
        val alpha = (a.value * 255).toInt()
        (alpha shl 24) or (red shl 16) or (green shl 8) or blue
    }
}

private fun Int.toUIColor(): UIColor {
    // 비트 마스킹으로 각 성분 추출 (0xAARRGGBB 기준)
    val a = (this shr 24 and 0xFF).toDouble() / 255.0
    val r = (this shr 16 and 0xFF).toDouble() / 255.0
    val g = (this shr 8 and 0xFF).toDouble() / 255.0
    val b = (this and 0xFF).toDouble() / 255.0

    return UIColor.colorWithRed(
        red = r,
        green = g,
        blue = b,
        alpha = a,
    )
}
