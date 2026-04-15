package com.daedan.festabook.presentation.waitinginfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.model.WaitingInfo
import com.daedan.festabook.domain.repository.WaitingInfoRepository
import com.daedan.festabook.presentation.waitinginfo.model.WaitingInfoUiState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class)
@ViewModelKey(WaitingInfoViewModel::class)
@Inject
class WaitingInfoViewModel(
    private val waitingInfoRepository: WaitingInfoRepository,
) : ViewModel() {
    private val _waitingInfoUiState =
        MutableStateFlow<WaitingInfoUiState>(WaitingInfoUiState.Loading)
    val waitingInfoUiState: StateFlow<WaitingInfoUiState> = _waitingInfoUiState.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _isTermsAgreed = MutableStateFlow(false)
    val isTermsAgreed: StateFlow<Boolean> = _isTermsAgreed.asStateFlow()

    private val isSaving = MutableStateFlow(false)

    val isSaveEnabled: StateFlow<Boolean> =
        combine(
            _phoneNumber,
            _isTermsAgreed,
            isSaving,
        ) { phone, terms, saving ->
            phone.count { it.isDigit() } >= 9 && terms && !saving
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    private val _saveSuccessEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val saveSuccessEvent: SharedFlow<Unit> = _saveSuccessEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<Throwable>(replay = 0, extraBufferCapacity = 1)
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    fun updatePhoneNumber(input: String) {
        if (input.count() > 11) return
        _phoneNumber.value = input.filter { it.isDigit() }
    }

    fun setTermsAgreed(agreed: Boolean) {
        _isTermsAgreed.value = agreed
    }

    fun saveWaitingInfo(phoneNumber: String) {
        if (isSaving.value) return
        val currentState = _waitingInfoUiState.value
        if (currentState is WaitingInfoUiState.Loading) return

        viewModelScope.launch {
            isSaving.value = true
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
                    isSaving.value = false
                }
        }
    }

    fun loadWaitingInfo() {
        viewModelScope.launch {
            _waitingInfoUiState.value = WaitingInfoUiState.Loading
            waitingInfoRepository
                .getWaitingInfo()
                .onSuccess { waitingInfo ->
                    if (waitingInfo != null) {
                        _phoneNumber.value = waitingInfo.phoneNumber
                        _isTermsAgreed.value = true
                        _waitingInfoUiState.value =
                            WaitingInfoUiState.Registered(waitingInfo.phoneNumber)
                    } else {
                        _waitingInfoUiState.value = WaitingInfoUiState.NotRegistered
                    }
                }.onFailure { throwable ->
                    _waitingInfoUiState.value = WaitingInfoUiState.Error(throwable)
                    _errorEvent.emit(throwable)
                }
        }
    }
}
