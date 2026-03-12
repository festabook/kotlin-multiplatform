package com.daedan.festabook.presentation.placeMap.platform

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
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual class NaverMap(
    val platformMap: NMFNaverMapView,
) {
    private var _locationSource: LocationSource? = null
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
        platformMap.mapView.addCameraDelegate(
            object : NSObject(), NMFMapViewCameraDelegateProtocol {
                override fun mapView(
                    mapView: NMFMapView,
                    cameraDidChangeByReason: Long,
                    animated: Boolean,
                ) {
                    listener.onCameraChange(cameraDidChangeByReason.toInt(), animated)
                }
            },
        )
    }

    actual fun addOnLocationChangeListener(listener: OnLocationChangeListener) {
        NMFLocationManager.sharedInstance()?.addDelegate(
            object : NSObject(), NMFLocationManagerDelegateProtocol {
                override fun locationManager(
                    locationManager: NMFLocationManager?,
                    didUpdateLocations: List<*>?,
                ) {
                    listener.onLocationChange()
                }
            },
        )
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
        platformMap.mapView.touchDelegate =
            object : NSObject(), NMFMapViewTouchDelegateProtocol {
                override fun mapView(
                    mapView: NMFMapView,
                    didLongTapMap: NMGLatLng,
                    point: CValue<CGPoint>,
                ) {
                    onClick(LatLng(didLongTapMap.lat(), didLongTapMap.lng()))
                }
            }
    }

    actual fun setContentPadding(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        animate: Boolean,
    ) {
        platformMap.mapView.contentInset =
            cValue<UIEdgeInsets> {
                this.left = left.toDouble()
                this.top = top.toDouble()
                this.right = right.toDouble()
                this.bottom = bottom.toDouble()
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
