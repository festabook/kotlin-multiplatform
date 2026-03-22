package com.daedan.festabook.presentation

import dev.zacsweers.metro.Inject
import platform.Foundation.NSBundle
import platform.UIKit.UIApplication

@Inject
actual class ContextFactory {
    actual fun createActivityContext(): Any = NSBundle

    actual fun createApplicationContext(): Any = UIApplication

    actual fun createActivity(): Any = UIApplication.sharedApplication.keyWindow?.rootViewController ?: ""
}
