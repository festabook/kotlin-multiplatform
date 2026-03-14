package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable

@Composable
expect fun PlaceDetailPreviewBackHandler(
    enabled: Boolean,
    onBackPress: () -> Unit,
)
