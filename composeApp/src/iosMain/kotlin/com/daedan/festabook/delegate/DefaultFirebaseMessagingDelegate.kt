package com.daedan.festabook.delegate

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.daedan.festabook.DefaultFestabookAppDelegate
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.Foundation.NSLog
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class DefaultFirebaseMessagingDelegate(
    private val scope: CoroutineScope,
) : NSObject(),
    FIRMessagingDelegateProtocol {
    private val deviceRepository = DefaultFestabookAppDelegate.appGraph.deviceRepository

    override fun messaging(
        messaging: FIRMessaging,
        didReceiveRegistrationToken: String?,
    ) {
        // FCM 토큰 등록
        didReceiveRegistrationToken?.let {
            scope.launch {
                val result = deviceRepository.registerDevice(it)
                NSLog(result.toString())
            }
        }
    }
}
