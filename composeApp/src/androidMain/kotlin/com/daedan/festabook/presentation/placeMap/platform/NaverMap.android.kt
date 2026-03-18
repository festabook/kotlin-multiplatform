package com.daedan.festabook.presentation.placeMap.platform

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.naver.maps.map.NaverMap as PlatformMap
import com.naver.maps.map.UiSettings as PlatformUiSettings

actual class NaverMap(
    val platformMap: PlatformMap,
) {
    val platformUiSettings = platformMap.uiSettings

    private var _locationSource: LocationSource? = null
    actual var locationSource: LocationSource?
        get() = _locationSource
        set(value) {
            _locationSource = value
            platformMap.locationSource = value?.platform
        }

    actual var isIndoorEnabled: Boolean
        get() = platformMap.isIndoorEnabled
        set(value) {
            platformMap.isIndoorEnabled = value
        }
    actual var symbolScale: Double
        get() = platformMap.symbolScale.toDouble()
        set(value) {
            platformMap.symbolScale = value.toFloat()
        }

    actual var customStyleId: String?
        get() = platformMap.customStyleId
        set(value) {
            platformMap.customStyleId = value
        }
    actual val uiSettings: UiSettings = UiSettings(platformUiSettings)
    actual val cameraPosition: CameraPosition
        get() = CameraPosition(platformMap.cameraPosition)

    actual fun moveCamera(cameraUpdate: CameraUpdate) {
        platformMap.moveCamera(cameraUpdate.platform)
    }

    actual fun addOnCameraChangeListener(listener: OnCameraChangeListener) {
        platformMap.addOnCameraChangeListener { reason, animated ->
            listener.onCameraChange(reason, animated)
        }
    }

    actual fun addOnLocationChangeListener(listener: OnLocationChangeListener) {
        platformMap.addOnLocationChangeListener { _ ->
            listener.onLocationChange()
        }
    }

    actual fun setOnMapClickListener(onClick: (LatLng) -> Unit) {
        platformMap.setOnMapClickListener { pointF, latLng ->
            onClick(LatLng(latLng.latitude, latLng.longitude))
        }
    }

    actual fun setContentPadding(
        left: Dp,
        top: Dp,
        right: Dp,
        bottom: Dp,
        density: Density,
        animate: Boolean,
    ) {
        with(density) {
            platformMap.setContentPadding(
                left.toPx().toInt(),
                top.toPx().toInt(),
                right.toPx().toInt(),
                bottom.toPx().toInt(),
                animate,
            )
        }
    }

    actual fun interface OnCameraChangeListener {
        actual fun onCameraChange(
            reason: Int,
            animated: Boolean,
        )
    }

    actual fun interface OnLocationChangeListener {
        actual fun onLocationChange()
    }

    actual class UiSettings(
        private val platformUiSettings: PlatformUiSettings,
    ) {
        actual var isZoomControlEnabled: Boolean
            get() = platformUiSettings.isZoomControlEnabled
            set(value) {
                platformUiSettings.isZoomControlEnabled = value
            }
        actual var isScaleBarEnabled: Boolean
            get() = platformUiSettings.isScaleBarEnabled
            set(value) {
                platformUiSettings.isScaleBarEnabled = value
            }

        actual fun setLogoMargin(
            start: Int,
            top: Int,
            end: Int,
            bottom: Int,
        ) {
            platformUiSettings.setLogoMargin(start, top, end, bottom)
        }
    }
}
