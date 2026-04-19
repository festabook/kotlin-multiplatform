package com.daedan.festabook.presentation.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.repository.MyWaitingRepository
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.toUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class)
@ViewModelKey(MyWaitingViewModel::class)
@Inject
class MyWaitingViewModel(
    private val myWaitingRepository: MyWaitingRepository,
    private val placeDetailRepository: PlaceDetailRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyWaitingUiState>(MyWaitingUiState.Loading)
    val uiState: StateFlow<MyWaitingUiState> = _uiState.asStateFlow()

    private val _cancelSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val cancelSuccessEvent: SharedFlow<Unit> = _cancelSuccessEvent.asSharedFlow()

    private val _errorEvent = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    init {
        loadMyWaiting()
    }

    fun loadMyWaiting() {
        viewModelScope.launch {
            _uiState.value = MyWaitingUiState.Loading
            myWaitingRepository
                .getMyWaiting()
                .onSuccess { myWaiting ->
                    if (myWaiting == null) {
                        _uiState.value = MyWaitingUiState.Empty
                        return@onSuccess
                    }
                    _uiState.value =
                        myWaiting.toSuccessUiState(
                            placeDetail = fetchPlaceDetail(myWaiting.placeId),
                        )
                }.onFailure { _uiState.value = MyWaitingUiState.Error(it) }
        }
    }

    fun refresh() {
        val current = _uiState.value as? MyWaitingUiState.Success ?: return
        _uiState.value = current.copy(isRefreshing = true)
        viewModelScope.launch {
            myWaitingRepository
                .getMyWaiting()
                .onSuccess { myWaiting ->
                    if (myWaiting == null) {
                        _uiState.value = MyWaitingUiState.Empty
                        return@onSuccess
                    }
                    _uiState.value =
                        myWaiting.toSuccessUiState(
                            placeDetail = fetchPlaceDetail(myWaiting.placeId),
                            isRefreshing = false,
                        )
                }.onFailure {
                    _uiState.update { state ->
                        (state as? MyWaitingUiState.Success)?.copy(isRefreshing = false) ?: state
                    }
                    _errorEvent.tryEmit(it)
                }
        }
    }

    fun cancelWaiting() {
        val current = _uiState.value as? MyWaitingUiState.Success ?: return
        _uiState.value = current.copy(isCanceling = true)
        viewModelScope.launch {
            myWaitingRepository
                .cancelWaiting(current.waitingId)
                .onSuccess {
                    _uiState.update { state ->
                        (state as? MyWaitingUiState.Success)?.copy(isCanceling = false) ?: state
                    }
                    _cancelSuccessEvent.tryEmit(Unit)
                }.onFailure {
                    _uiState.update { state ->
                        (state as? MyWaitingUiState.Success)?.copy(isCanceling = false) ?: state
                    }
                    _errorEvent.tryEmit(it)
                }
        }
    }

    private suspend fun fetchPlaceDetail(placeId: Long): PlaceDetailUiModel? =
        placeDetailRepository
            .getPlaceDetail(placeId)
            .getOrNull()
            ?.toUiModel()
}

private fun MyWaiting.toSuccessUiState(
    placeDetail: PlaceDetailUiModel?,
    isRefreshing: Boolean = false,
): MyWaitingUiState.Success =
    MyWaitingUiState.Success(
        waitingId = waitingId,
        placeId = placeId,
        placeDetail = placeDetail,
        order = waitingOrder,
        partySize = partySize,
        phoneNumber = phoneNumber,
        totalWaitingTeams = totalWaitingTeams,
        estimatedWaitTime = estimatedWaitTime,
        status = waitingStatus,
        isRefreshing = isRefreshing,
    )