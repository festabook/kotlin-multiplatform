package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import com.naver.maps.map.widget.LocationButtonView

@Composable
actual fun CurrentLocationButton(
    modifier: Modifier,
    map: NaverMap?,
    visible: Boolean,
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> LocationButtonView(context) },
        update = { view -> view.map = map?.platformMap },
    )
}
