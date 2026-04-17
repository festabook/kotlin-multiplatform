package com.daedan.festabook.presentation.placeMap.placeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.daedan.festabook.domain.model.PlaceWaiting
import com.daedan.festabook.domain.repository.MyWaitingRepository
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.ImageUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingStatusUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingTeamUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.toUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AssistedInject
class PlaceDetailViewModel(
    private val placeDetailRepository: PlaceDetailRepository,
    private val waitingRegisterInfoRepository: WaitingRegisterInfoRepository,
    private val myWaitingRepository: MyWaitingRepository,
    @Assisted private val placeId: Long,
) : ViewModel() {
    @AssistedFactory
    @ViewModelAssistedFactoryKey(PlaceDetailViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): PlaceDetailViewModel =
            create(
                placeId = extras[PlaceIdKey] ?: error("placeId must be required"),
            )

        fun create(placeId: Long): PlaceDetailViewModel
    }

    private val _placeDetail =
        MutableStateFlow<PlaceDetailUiState>(
            PlaceDetailUiState.Loading,
        )
    val placeDetail: StateFlow<PlaceDetailUiState> = _placeDetail

    private val _navigateToWaitingRegisterEvent = MutableSharedFlow<Long>(replay = 0, extraBufferCapacity = 1)
    val navigateToWaitingRegisterEvent: SharedFlow<Long> = _navigateToWaitingRegisterEvent.asSharedFlow()

    private val _showDuplicateWaitingDialogEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val showDuplicateWaitingDialogEvent: SharedFlow<Unit> = _showDuplicateWaitingDialogEvent.asSharedFlow()

    init {
        loadPlaceDetail(placeId)
    }

    fun loadPlaceDetail(placeId: Long) {
        viewModelScope.launch {
            val result = placeDetailRepository.getPlaceDetail(placeId)
            result
                .onSuccess { placeDetail ->
                    val placeDetailUiModel =
                        if (placeDetail.sortedImages.isEmpty()) {
                            placeDetail.toUiModel().copy(images = listOf(ImageUiModel()))
                        } else {
                            placeDetail.toUiModel()
                        }
                    _placeDetail.value =
                        PlaceDetailUiState.Success(placeDetailUiModel)
                    loadWaitingStatus()
                }.onFailure { throwable ->
                    _placeDetail.value = PlaceDetailUiState.Error(throwable)
                }
        }
    }

    suspend fun refreshWaitingStatus() {
        updateInnerState { current ->
            val previousTeams = (current.waitingTeam as? WaitingTeamUiState.Success)?.totalTeams ?: 0
            current.copy(waitingTeam = WaitingTeamUiState.Refresh(totalTeams = previousTeams))
        }
        val placeDetailState = _placeDetail.value
        if (placeDetailState !is PlaceDetailUiState.Success) return
        val placeId = placeDetailState.placeDetail.place.id
        val waitingResult = waitingRegisterInfoRepository.getPlaceWaiting(placeId)

        waitingResult
            .onSuccess { placeWaiting ->
                val waitingTeamUiState =
                    WaitingTeamUiState.Success(totalTeams = placeWaiting.totalWaitingTeams)

                updateInnerState { current ->
                    current.copy(
                        waitingTeam = waitingTeamUiState,
                    )
                }
            }.onFailure { throwable ->
                updateInnerState { current ->
                    current.copy(
                        waitingTeam = WaitingTeamUiState.Error(throwable),
                    )
                }
            }
    }

    fun toggleNoticeExpanded(notice: NoticeUiModel) {
        _placeDetail.update { current ->
            if (current !is PlaceDetailUiState.Success) return@update current
            current.copy(
                placeDetail =
                    current.placeDetail.copy(
                        notices =
                            current.placeDetail.notices.map {
                                if (notice.id == it.id) it.copy(isExpanded = !it.isExpanded) else it
                            },
                    ),
            )
        }
    }

    fun onRegisterWaitingClick() {
        val current = _placeDetail.value
        if (current !is PlaceDetailUiState.Success) return
        val placeId = current.placeDetail.place.id

        viewModelScope.launch {
            myWaitingRepository
                .getMyWaiting()
                .onSuccess { myWaiting ->
                    if (myWaiting == null) {
                        _navigateToWaitingRegisterEvent.tryEmit(placeId)
                    } else {
                        _showDuplicateWaitingDialogEvent.tryEmit(Unit)
                    }
                }.onFailure {
                    _navigateToWaitingRegisterEvent.tryEmit(placeId)
                }
        }
    }

    // TODO UseCase 혹은 Domain Layer로 이동, 하지만 PlaceUiModel -> Place로 변환 불가, 아키텍쳐 변화 필요
    private fun PlaceDetailUiState.isWaitingNotSupported(placeWaiting: PlaceWaiting): Boolean =
        if (this is PlaceDetailUiState.Success) {
            !placeDetail.isWaitingActive && placeWaiting.totalWaitingTeams == 0
        } else {
            true
        }

    private suspend fun loadWaitingStatus() {
        val placeDetailState = _placeDetail.value
        if (placeDetailState !is PlaceDetailUiState.Success) return
        val placeId = placeDetailState.placeDetail.place.id
        val waitingResult = waitingRegisterInfoRepository.getPlaceWaiting(placeId)
        val isWaitingActive = placeDetailState.placeDetail.isWaitingActive

        waitingResult
            .onSuccess { placeWaiting ->
                val isWaitingNotSupported = placeDetailState.isWaitingNotSupported(placeWaiting)
                val waitingTeamUiState =
                    WaitingTeamUiState.Success(totalTeams = placeWaiting.totalWaitingTeams)

                val waitingStatusUiState =
                    if (isWaitingActive) {
                        WaitingStatusUiState.Active(
                            estimatedMinutes = placeWaiting.estimatedWaitTime,
                        )
                    } else {
                        WaitingStatusUiState.Closed(
                            estimatedMinutes = placeWaiting.estimatedWaitTime,
                        )
                    }
                updateInnerState { current ->
                    current.copy(
                        waitingTeam = if (isWaitingNotSupported) WaitingTeamUiState.InActive else waitingTeamUiState,
                        waitingStatus = if (isWaitingNotSupported) WaitingStatusUiState.InActive else waitingStatusUiState,
                    )
                }
            }.onFailure { throwable ->
                _placeDetail.value = PlaceDetailUiState.Error(throwable)
            }
    }

    private fun updateInnerState(onUpdate: (PlaceDetailUiState.Success) -> PlaceDetailUiState.Success) {
        _placeDetail.update { current ->
            if (current is PlaceDetailUiState.Success) {
                onUpdate(current)
            } else {
                current
            }
        }
    }

    object PlaceIdKey : CreationExtras.Key<Long>
}
