package com.daedan.festabook.presentation.setting.waitinginfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface WaitingInfoUiState {
    data object Loading : WaitingInfoUiState
    data class Registered(val phoneNumber: String) : WaitingInfoUiState
    data object NotRegistered : WaitingInfoUiState
    data class Error(val throwable: Throwable) : WaitingInfoUiState
}

@ContributesIntoMap(AppScope::class)
@ViewModelKey(WaitingInfoViewModel::class)
@Inject
class WaitingInfoViewModel(
    private val waitingInfoRepository: WaitingInfoRepository,
) : ViewModel() {
    private val _waitingInfoUiState = MutableStateFlow<WaitingInfoUiState>(WaitingInfoUiState.Loading)
    val waitingInfoUiState: StateFlow<WaitingInfoUiState> = _waitingInfoUiState.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveSuccessEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val saveSuccessEvent: SharedFlow<Unit> = _saveSuccessEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<Throwable>(replay = 0, extraBufferCapacity = 1)
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    init {
        loadWaitingInfo()
    }

    fun loadWaitingInfo() {
        viewModelScope.launch {
            _waitingInfoUiState.value = WaitingInfoUiState.Loading
            waitingInfoRepository
                .getWaitingInfo()
                .onSuccess { waitingInfo ->
                    _waitingInfoUiState.value =
                        if (waitingInfo != null) {
                            WaitingInfoUiState.Registered(waitingInfo.phoneNumber)
                        } else {
                            WaitingInfoUiState.NotRegistered
                        }
                }.onFailure { throwable ->
                    _waitingInfoUiState.value = WaitingInfoUiState.Error(throwable)
                    _errorEvent.emit(throwable)
                }
        }
    }

    fun saveWaitingInfo(phoneNumber: String) {
        if (_isSaving.value) return
        val currentState = _waitingInfoUiState.value
        if (currentState is WaitingInfoUiState.Loading) return

        viewModelScope.launch {
            _isSaving.value = true
            val result =
                if (currentState is WaitingInfoUiState.Registered) {
                    waitingInfoRepository.updateWaitingInfo(WaitingInfo(phoneNumber))
                } else {
                    waitingInfoRepository.saveWaitingInfo(WaitingInfo(phoneNumber))
                }
            result
                .onSuccess {
                    // 낙관적 업데이트: reload 없이 직접 상태 반영
                    _waitingInfoUiState.value = WaitingInfoUiState.Registered(phoneNumber)
                    _saveSuccessEvent.emit(Unit)
                }.onFailure { throwable ->
                    _errorEvent.emit(throwable)
                }.also {
                    _isSaving.value = false
                }
        }
    }
}