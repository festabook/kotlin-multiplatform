package com.daedan.festabook.presentation.placeMap.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun PlaceDetailPreviewBackHandler(
    enabled: Boolean,
    onBackPress: () -> Unit,
) {
    BackHandler(enabled = enabled) {
        onBackPress()
    }
}
