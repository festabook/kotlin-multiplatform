package com.daedan.festabook.delegate

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.zacsweers.metro.Inject
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, FlowPreview::class)
@Inject
class DefaultFirebaseMessagingDelegate(
    private val scope: CoroutineScope,
    private val deviceRepository: DeviceRepository,
) : NSObject(),
    FIRMessagingDelegateProtocol {
    override fun messaging(
        messaging: FIRMessaging,
        didReceiveRegistrationToken: String?,
    ) {
//        Napier.d("Refreshed token: $didReceiveRegistrationToken")
    }

    fun registerFcmToken() {
        FIRMessaging.messaging().tokenWithCompletion { token, error ->
            if (error != null) Napier.e("fcmTokenError: $error")
            Napier.d("$token")

            token?.let {
                scope.launch {
                    deviceRepository.registerDevice(it)
                }
            }
        }
    }
}
