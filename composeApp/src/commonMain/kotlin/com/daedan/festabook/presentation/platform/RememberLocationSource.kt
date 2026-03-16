package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.placeMap.platform.LocationSource

@Composable
expect fun rememberLocationSource(): LocationSource
