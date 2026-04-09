package com.daedan.festabook.delegate

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Inject
class DefaultFirebaseMessagingDelegate(
    @param:Named("Main") private val scope: CoroutineScope,
    private val deviceRepository: DeviceRepository,
) : NSObject(),
    FIRMessagingDelegateProtocol {
    override fun messaging(
        messaging: FIRMessaging,
        didReceiveRegistrationToken: String?,
    ) {
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
