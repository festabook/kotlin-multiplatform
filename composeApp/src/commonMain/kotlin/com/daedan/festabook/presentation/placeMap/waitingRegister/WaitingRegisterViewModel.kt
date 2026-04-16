package com.daedan.festabook.presentation.placeMap.waitingRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
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

    init {
        loadPlaceSummary()
    }

    fun loadPlaceSummary() {
        viewModelScope.launch {
            _uiState.value = WaitingRegisterUiState.Loading
            placeDetailRepository
                .getPlaceDetail(placeId)
                .onSuccess { placeDetail ->
                    _uiState.value = WaitingRegisterUiState.Success(
                        placeSummary = placeDetail.toWaitingPlaceSummaryUiModel(),
                    )
                }.onFailure { throwable ->
                    _uiState.value = WaitingRegisterUiState.Error(throwable)
                }
        }
    }

    fun increasePartySize() {
        _uiState.update { current ->
            if (current !is WaitingRegisterUiState.Success) return@update current
            if (!current.canIncreasePartySize) return@update current
            current.copy(partySize = current.partySize + 1)
        }
    }

    fun decreasePartySize() {
        _uiState.update { current ->
            if (current !is WaitingRegisterUiState.Success) return@update current
            if (!current.canDecreasePartySize) return@update current
            current.copy(partySize = current.partySize - 1)
        }
    }

    fun toggleServiceAgreement() {
        _uiState.update { current ->
            if (current !is WaitingRegisterUiState.Success) return@update current
            current.copy(isServiceAgreed = !current.isServiceAgreed)
        }
    }

    fun toggleMarketingAgreement() {
        _uiState.update { current ->
            if (current !is WaitingRegisterUiState.Success) return@update current
            current.copy(isMarketingAgreed = !current.isMarketingAgreed)
        }
    }

    fun submitWaitingRegister() {
        val current = _uiState.value
        if (current !is WaitingRegisterUiState.Success) return
        if (!current.canSubmit) return

        viewModelScope.launch {
            _uiState.update { state ->
                if (state is WaitingRegisterUiState.Success) {
                    state.copy(isSubmitting = true)
                } else {
                    state
                }
            }
            // NOTE: registerWaiting API에는 phoneNumber 파라미터가 없음.
            // 서버가 인증 토큰 기반으로 전화번호를 관리한다고 가정함.
            // 실제 전화번호 선행 등록 플로우 확인 필요 (open-questions.md 참조)
            runCatching {
                waitingRegisterInfoRepository.registerWaiting(
                    placeId = placeId,
                    partySize = current.partySize,
                ).getOrThrow()
            }.onSuccess {
                _uiState.update { state ->
                    if (state is WaitingRegisterUiState.Success) {
                        state.copy(isSubmitting = false)
                    } else {
                        state
                    }
                }
                _registerSuccessEvent.tryEmit(Unit)
            }.onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                _uiState.update { state ->
                    if (state is WaitingRegisterUiState.Success) {
                        state.copy(isSubmitting = false)
                    } else {
                        state
                    }
                }
                _registerFailureEvent.tryEmit(throwable)
            }
        }
    }

    object PlaceIdKey : CreationExtras.Key<Long>
}
