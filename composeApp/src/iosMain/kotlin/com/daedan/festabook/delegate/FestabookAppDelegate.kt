package com.daedan.festabook.delegate

import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundFetchResult

/**
 * Swift 단에서 사용되는 메서드를 인터페이스로 따로 분리한 클래스입니다.
 * Objc 클래스를 상속한 클래스는 스위프트에서 사용이 불가능하여 별도의 인터페이스로 분리합니다.
 */

interface FestabookAppDelegate : AppGraphDelegate {
    fun application(
        application: UIApplication,
        launchOptions: Map<Any?, *>?,
    ): Boolean = true

    fun application(
        application: UIApplication,
        deviceToken: NSData,
    )

    fun application(
        application: UIApplication,
        userInfo: Map<Any?, *>,
        fetchCompletionHandler: (UIBackgroundFetchResult) -> Unit,
    )
}
