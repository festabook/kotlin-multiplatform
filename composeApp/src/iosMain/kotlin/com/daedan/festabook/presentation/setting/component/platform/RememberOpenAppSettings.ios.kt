package com.daedan.festabook.presentation.setting.component.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun rememberOpenAppSettings(): () -> Unit =
    remember {
        {
            val url =
                NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return@remember
            UIApplication.sharedApplication.openURL(url, mapOf<Any?, Any?>(), null)
        }
    }
