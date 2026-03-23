package com.daedan.festabook.presentation.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberPermissionLauncher(onResult: (Boolean) -> Unit): (String) -> Unit

expect fun shouldShowRationale(
    permission: String,
    activity: Any?,
): Boolean
