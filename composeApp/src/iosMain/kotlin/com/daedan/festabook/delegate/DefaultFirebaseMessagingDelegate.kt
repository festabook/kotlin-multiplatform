package com.daedan.festabook.delegate

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.daedan.festabook.DefaultFestabookAppDelegate
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
        FIRMessaging.messaging().tokenWithCompletion { token, error ->
            token?.let {
                println("최신 토큰: $it")
                println("error: $error")
                scope.launch {
                    deviceRepository.registerDevice(it)
                }
            }
        }
    }
}
