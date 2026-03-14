package com.daedan.festabook.presentation.platform

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.daedan.festabook.presentation.placeMap.platform.LocationSource
import com.naver.maps.map.util.FusedLocationSource

private const val LOCATION_PERMISSION_REQUEST_CODE = 1234

@Composable
actual fun rememberLocationSource(): LocationSource {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    return remember(activity) {
        LocationSource(
            platform = FusedLocationSource(activity, LOCATION_PERMISSION_REQUEST_CODE),
        )
    }
}
