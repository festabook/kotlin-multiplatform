package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.presentation.placeMap.platform.NaverMap

@Composable
expect fun CurrentLocationButton(
    modifier: Modifier = Modifier,
    map: NaverMap? = null,
)
