package com.daedan.festabook.presentation.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.model.MyWaiting
import com.daedan.festabook.domain.model.PlaceDetail
import com.daedan.festabook.domain.repository.MyWaitingRepository
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.presentation.placeMap.model.PlaceCategoryUiModel
import com.daedan.festabook.presentation.placeMap.model.toUiModel
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
                .onSuccess { withPlace ->
                    if (withPlace == null) {
                        _uiState.value = MyWaitingUiState.Empty
                        return@onSuccess
                    }
                    val place =
                        withPlace.placeId?.let { pid ->
                            placeDetailRepository
                                .getPlaceDetail(pid)
                                .getOrNull()
                                ?.toMyWaitingPlaceUiModel()
                        }
                    _uiState.value =
                        withPlace.toSuccessUiState(
                            placeId = withPlace.placeId,
                            place = place,
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
                    val place =
                        myWaiting.placeId?.let { pid ->
                            placeDetailRepository
                                .getPlaceDetail(pid)
                                .getOrNull()
                                ?.toMyWaitingPlaceUiModel()
                        }
                    _uiState.value =
                        myWaiting.toSuccessUiState(
                            placeId = myWaiting.placeId,
                            place = place,
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
}

private fun PlaceDetail.toMyWaitingPlaceUiModel(): MyWaitingPlaceUiModel =
    MyWaitingPlaceUiModel(
        title = place.title.orEmpty(),
        category = place.category.toUiModel(),
        imageUrl = place.imageUrl,
        location = place.location,
        host = host,
        operatingTime = if (startTime != null && endTime != null) "$startTime ~ $endTime" else null,
    )

private fun MyWaiting.toSuccessUiState(
    placeId: Long?,
    place: MyWaitingPlaceUiModel?,
    isRefreshing: Boolean = false,
): MyWaitingUiState.Success =
    MyWaitingUiState.Success(
        waitingId = waitingId,
        placeId = placeId,
        place = place,
        order = waitingOrder,
        partySize = partySize,
        phoneNumber = phoneNumber,
        totalWaitingTeams = totalWaitingTeams,
        estimatedWaitTime = estimatedWaitTime,
        status = waitingStatus,
        isRefreshing = isRefreshing,
    )
