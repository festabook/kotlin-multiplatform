package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.NMapsMap.NMFNaverMapView
import com.daedan.festabook.presentation.placeMap.intent.state.MapDelegate
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NaverMapContent(
    modifier: Modifier,
    mapDelegate: MapDelegate,
    onMapDrag: () -> Unit,
    onMapReady: (NaverMap) -> Unit,
    content: @Composable (NaverMap?) -> Unit,
) {
    val mapView = remember { NMFNaverMapView() }
    val naverMap = remember(mapView) { NaverMap(mapView) }
    val currentOnMapReady by rememberUpdatedState(onMapReady)

    LaunchedEffect(mapView) {
        currentOnMapReady(naverMap)
        mapDelegate.initMap(naverMap)
    }

    Box(modifier = modifier) {
        UIKitView(
            factory = { mapView },
            modifier = Modifier.dragInterceptor(onMapDrag),
        )
        content(mapDelegate.value)
    }
}
