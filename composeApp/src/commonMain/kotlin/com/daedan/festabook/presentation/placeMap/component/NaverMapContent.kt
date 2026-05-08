package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.presentation.placeMap.intent.state.MapDelegate
import com.daedan.festabook.presentation.placeMap.platform.NaverMap

@Composable
expect fun NaverMapContent(
    modifier: Modifier = Modifier,
    mapDelegate: MapDelegate = MapDelegate(),
    onMapDrag: () -> Unit = {},
    onMapReady: (NaverMap) -> Unit = {},
    isVisible: Boolean = true,
    content: @Composable (NaverMap?) -> Unit,
)
