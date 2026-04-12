package com.daedan.festabook.delegate

import cocoapods.FirebaseMessaging.FIRMessaging
import cocoapods.FirebaseMessaging.FIRMessagingDelegateProtocol
import com.daedan.festabook.domain.repository.DeviceRepository
import dev.zacsweers.metro.Inject
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class, FlowPreview::class)
@Inject
class DefaultFirebaseMessagingDelegate(
    private val scope: CoroutineScope,
    private val deviceRepository: DeviceRepository,
) : NSObject(),
    FIRMessagingDelegateProtocol {
    private val currentToken = MutableSharedFlow<String>(replay = 1)

    init {
        scope.launch {
            val savedToken = deviceRepository.getFcmToken()
            currentToken
                .debounce(3000L)
                .collectLatest { token ->
                    if (savedToken == token) return@collectLatest
                    deviceRepository.registerDevice(token)
                }
        }
    }

    override fun messaging(
        messaging: FIRMessaging,
        didReceiveRegistrationToken: String?,
    ) {
        didReceiveRegistrationToken?.let {
            scope.launch {
                currentToken.emit(it)
            }
        }
    }
}
