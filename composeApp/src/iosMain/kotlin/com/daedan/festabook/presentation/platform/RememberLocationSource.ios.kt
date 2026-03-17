package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.daedan.festabook.presentation.placeMap.platform.LocationSource

@Composable
actual fun rememberLocationSource(): LocationSource = remember { LocationSource() }
