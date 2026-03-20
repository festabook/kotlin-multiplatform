package com.daedan.festabook.presentation

import dev.zacsweers.metro.Inject
import platform.Foundation.NSBundle
import platform.UIKit.UIApplication

@Inject
actual class ContextFactory {
    actual fun getContext(): Any = NSBundle

    actual fun getApplication(): Any = UIApplication

    actual fun getActivity(): Any = UIApplication.sharedApplication.keyWindow?.rootViewController ?: ""
}
