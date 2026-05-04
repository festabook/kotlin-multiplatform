package com.daedan.festabook.presentation.common

import androidx.compose.runtime.Composable
import com.daedan.festabook.logging.FirebaseAnalytics
import com.daedan.festabook.presentation.platform.rememberAppGraph

@Composable
fun rememberAnalytics(): FirebaseAnalytics = rememberAppGraph().analytics
