package com.daedan.festabook.presentation.placeMap.waitingRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiState
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.toWaitingPlaceSummaryUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AssistedInject
class WaitingRegisterViewModel(
    private val placeDetailRepository: PlaceDetailRepository,
    private val waitingInfoRepository: WaitingInfoRepository,
    private val waitingRegisterInfoRepository: WaitingRegisterInfoRepository,
    @Assisted private val placeId: Long,
) : ViewModel() {
    @AssistedFactory
    @ViewModelAssistedFactoryKey(WaitingRegisterViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): WaitingRegisterViewModel =
            create(placeId = extras[PlaceIdKey] ?: error("placeId must be required"))

        fun create(placeId: Long): WaitingRegisterViewModel
    }

    private val _uiState = MutableStateFlow<WaitingRegisterUiState>(WaitingRegisterUiState.Loading)
    val uiState: StateFlow<WaitingRegisterUiState> = _uiState.asStateFlow()

    private val _registerSuccessEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val registerSuccessEvent: SharedFlow<Unit> = _registerSuccessEvent.asSharedFlow()

    private val _registerFailureEvent = MutableSharedFlow<Throwable>(replay = 0, extraBufferCapacity = 1)
    val registerFailureEvent: SharedFlow<Throwable> = _registerFailureEvent.asSharedFlow()

    private val _navigateToPhoneRegistrationEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val navigateToPhoneRegistrationEvent: SharedFlow<Unit> = _navigateToPhoneRegistrationEvent.asSharedFlow()

    init {
        loadPlaceSummary()
    }

    fun loadPlaceSummary() {
        viewModelScope.launch {
            _uiState.value = WaitingRegisterUiState.Loading
            val waitingInfo = waitingInfoRepository.getWaitingInfo().getOrNull()
            if (waitingInfo == null) {
                _navigateToPhoneRegistrationEvent.tryEmit(Unit)
                return@launch
            }
            placeDetailRepository
                .getPlaceDetail(placeId)
                .onSuccess { placeDetail ->
                    _uiState.value =
                        WaitingRegisterUiState.Success(
                            waitingRegister =
                                WaitingRegisterUiModel(
                                    placeSummary = placeDetail.toWaitingPlaceSummaryUiModel(),
                                ).withDerivedState(),
                        )
                }.onFailure { throwable ->
                    _uiState.value = WaitingRegisterUiState.Error(throwable)
                }
        }
    }

    fun increasePartySize() {
        updateRegister { current ->
            if (!current.canIncreasePartySize) return@updateRegister current
            current.copy(partySize = current.partySize + 1).withDerivedState()
        }
    }

    fun decreasePartySize() {
        updateRegister { current ->
            if (!current.canDecreasePartySize) return@updateRegister current
            current.copy(partySize = current.partySize - 1).withDerivedState()
        }
    }

    fun toggleServiceAgreement() {
        updateRegister { current ->
            current.copy(isServiceAgreed = !current.isServiceAgreed).withDerivedState()
        }
    }

    fun submitWaitingRegister() {
        val current = _uiState.value
        if (current !is WaitingRegisterUiState.Success) return
        if (!current.waitingRegister.canSubmit) return

        viewModelScope.launch {
            updateRegister { it.copy(isSubmitting = true).withDerivedState() }

            runCatching {
                waitingRegisterInfoRepository
                    .registerWaiting(
                        placeId = placeId,
                        partySize = current.waitingRegister.partySize,
                    ).getOrThrow()
            }.onSuccess {
                updateRegister { it.copy(isSubmitting = false).withDerivedState() }
                _registerSuccessEvent.tryEmit(Unit)
            }.onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                updateRegister { it.copy(isSubmitting = false).withDerivedState() }
                _registerFailureEvent.tryEmit(throwable)
            }
        }
    }

    private inline fun updateRegister(transform: (WaitingRegisterUiModel) -> WaitingRegisterUiModel) {
        _uiState.update { state ->
            if (state !is WaitingRegisterUiState.Success) return@update state
            state.copy(waitingRegister = transform(state.waitingRegister))
        }
    }

    // TODO 도메인 로직으로 이동
    private fun WaitingRegisterUiModel.withDerivedState(): WaitingRegisterUiModel =
        copy(
            canDecreasePartySize = partySize > WaitingRegisterUiModel.MIN_PARTY_SIZE,
            canIncreasePartySize = partySize < WaitingRegisterUiModel.MAX_PARTY_SIZE,
            canSubmit = isServiceAgreed && !isSubmitting,
        )

    object PlaceIdKey : CreationExtras.Key<Long>
}
