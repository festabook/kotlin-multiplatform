package com.daedan.festabook.presentation.placeMap.platform

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cocoapods.NMapsMap.NMFLocationManager
import cocoapods.NMapsMap.NMFLocationManagerDelegateProtocol
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMFMapViewCameraDelegateProtocol
import cocoapods.NMapsMap.NMFMapViewTouchDelegateProtocol
import cocoapods.NMapsMap.NMFNaverMapView
import cocoapods.NMapsMap.NMGLatLng
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGPoint
import platform.UIKit.UIEdgeInsets
import platform.UIKit.UIEdgeInsetsMake
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual class NaverMap(
    val platformMap: NMFNaverMapView,
) {
    private var _locationSource: LocationSource? = null

    private var currentTouchDelegate: NMFMapViewTouchDelegateProtocol? = null

    private var currentCameraDelegate: NMFMapViewCameraDelegateProtocol? = null

    private var currentLocationDelegate: NMFLocationManagerDelegateProtocol? = null

    actual var locationSource: LocationSource?
        get() = _locationSource
        set(value) {
            _locationSource = value
        }

    actual val cameraPosition: CameraPosition
        get() = CameraPosition(platformMap.mapView.cameraPosition)

    actual fun moveCamera(cameraUpdate: CameraUpdate) {
        platformMap.mapView.moveCamera(cameraUpdate.platform)
    }

    actual fun addOnCameraChangeListener(listener: OnCameraChangeListener) {
        val cameraDelegate =
            object : NSObject(), NMFMapViewCameraDelegateProtocol {
                override fun mapView(
                    mapView: NMFMapView,
                    cameraDidChangeByReason: Long,
                    animated: Boolean,
                ) {
                    listener.onCameraChange(cameraDidChangeByReason.toInt(), animated)
                }
            }
        currentCameraDelegate = cameraDelegate
        platformMap.mapView.addCameraDelegate(currentCameraDelegate!!)
    }

    actual fun addOnLocationChangeListener(listener: OnLocationChangeListener) {
        val locationManagerDelegate =
            object : NSObject(), NMFLocationManagerDelegateProtocol {
                override fun locationManager(
                    locationManager: NMFLocationManager?,
                    didUpdateLocations: List<*>?,
                ) {
                    listener.onLocationChange()
                }
            }
        currentLocationDelegate = locationManagerDelegate
        NMFLocationManager.sharedInstance()?.addDelegate(currentLocationDelegate)
    }

    actual var isIndoorEnabled: Boolean
        get() = platformMap.mapView.isIndoorMapEnabled()
        set(value) {
            platformMap.mapView.setIndoorMapEnabled(value)
        }
    actual var symbolScale: Double
        get() = platformMap.mapView.symbolScale()
        set(value) {
            platformMap.mapView.setSymbolScale(value)
        }
    actual var customStyleId: String?
        get() = platformMap.mapView.customStyleId
        set(value) {
            platformMap.mapView.setCustomStyleId(value)
        }
    actual val uiSettings: UiSettings = UiSettings(platformMap)

    actual fun setOnMapClickListener(onClick: (LatLng) -> Unit) {
        val onClickDelegate =
            object : NSObject(), NMFMapViewTouchDelegateProtocol {
                override fun mapView(
                    mapView: NMFMapView,
                    didTapMap: NMGLatLng,
                    point: CValue<CGPoint>,
                ) {
                    onClick(LatLng(didTapMap.lat(), didTapMap.lng()))
                }
            }
        currentTouchDelegate = onClickDelegate
        platformMap.mapView.touchDelegate = currentTouchDelegate
    }

    actual fun setContentPadding(
        left: Dp,
        top: Dp,
        right: Dp,
        bottom: Dp,
        density: Density,
        animate: Boolean,
    ) {
        platformMap.mapView.contentInset =
            UIEdgeInsetsMake(
                left = left.value.toDouble(),
                top = top.value.toDouble(),
                right = right.value.toDouble(),
                bottom = bottom.value.toDouble(),
            )
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
        private val map: NMFNaverMapView,
    ) {
        actual var isZoomControlEnabled: Boolean
            get() = map.showZoomControls
            set(value) {
                map.showZoomControls = value
            }
        actual var isScaleBarEnabled: Boolean
            get() = map.showScaleBar
            set(value) {
                map.showScaleBar = value
            }

        actual fun setLogoMargin(
            start: Int,
            top: Int,
            end: Int,
            bottom: Int,
        ) {
            map.mapView.setLogoMargin(
                cValue {
                    this.left = start.toDouble()
                    this.top = top.toDouble()
                    this.right = end.toDouble()
                    this.bottom = bottom.toDouble()
                },
            )
        }
    }
}
