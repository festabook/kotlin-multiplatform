package com.daedan.festabook.presentation.placeMap.placeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.domain.repository.WaitingRegisterInfoRepository
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.ImageUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AssistedInject
class PlaceDetailViewModel(
    private val placeDetailRepository: PlaceDetailRepository,
    private val waitingRegisterInfoRepository: WaitingRegisterInfoRepository,
    @Assisted private val place: PlaceUiModel?,
    @Assisted private val receivedPlaceDetail: PlaceDetailUiModel?,
) : ViewModel() {
    @AssistedFactory
    @ViewModelAssistedFactoryKey(PlaceDetailViewModel::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): PlaceDetailViewModel =
            create(
                place = extras[PlaceKey],
                receivedPlaceDetail = extras[PlaceDetailKey],
            )

        fun create(
            place: PlaceUiModel?,
            receivedPlaceDetail: PlaceDetailUiModel?,
        ): PlaceDetailViewModel
    }

    private val _placeDetail =
        MutableStateFlow<PlaceDetailUiState>(
            PlaceDetailUiState.Loading,
        )
    val placeDetail: StateFlow<PlaceDetailUiState> = _placeDetail

    init {
        receivedPlaceDetail?.let {
            val placeDetailUiModel =
                if (it.images.isEmpty()) it.copy(images = listOf(ImageUiModel())) else it
            _placeDetail.value = PlaceDetailUiState.Success(placeDetailUiModel)
            viewModelScope.launch {
                loadWaitingStatus()
            }
        }
        place?.let { loadPlaceDetail(it.id) }
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
        _placeDetail.update { current ->
            if (current is PlaceDetailUiState.Success) {
                current.copy(waitingTeam = WaitingTeamUiState.Loading)
            } else {
                current
            }
        }
        val placeDetailState = _placeDetail.value
        if (placeDetailState !is PlaceDetailUiState.Success) return
        val placeId = placeDetailState.placeDetail.place.id
        val waitingResult = waitingRegisterInfoRepository.getPlaceWaiting(placeId)

        waitingResult
            .onSuccess { placeWaiting ->
                val waitingTeamUiState =
                    WaitingTeamUiState.Success(totalTeams = placeWaiting.totalWaitingTeams)

                _placeDetail.update { current ->
                    if (current is PlaceDetailUiState.Success) {
                        current.copy(
                            waitingTeam = waitingTeamUiState,
                        )
                    } else {
                        current
                    }
                }
            }.onFailure { throwable ->
                _placeDetail.update { current ->
                    if (current is PlaceDetailUiState.Success) {
                        current.copy(
                            waitingTeam = WaitingTeamUiState.Error(throwable),
                        )
                    } else {
                        current
                    }
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

    private suspend fun loadWaitingStatus() {
        val placeDetailState = _placeDetail.value
        if (placeDetailState !is PlaceDetailUiState.Success) return
        val placeId = placeDetailState.placeDetail.place.id
        val waitingResult = waitingRegisterInfoRepository.getPlaceWaiting(placeId)
        val isWaitingActive = placeDetailState.placeDetail.isWaitingActive

        waitingResult
            .onSuccess { placeWaiting ->
                val waitingTeamUiState =
                    WaitingTeamUiState.Success(totalTeams = placeWaiting.totalWaitingTeams)

                val waitingStatusUiState =
                    if (isWaitingActive) {
                        WaitingStatusUiState.Active(
                            estimatedMinutes = placeWaiting.estimatedWaitTime,
                        )
                    } else {
                        WaitingStatusUiState.Closed
                    }
                _placeDetail.update { current ->
                    if (current is PlaceDetailUiState.Success) {
                        current.copy(
                            waitingTeam = waitingTeamUiState,
                            waitingStatus = waitingStatusUiState,
                        )
                    } else {
                        current
                    }
                }
            }.onFailure { throwable ->
                _placeDetail.value = PlaceDetailUiState.Error(throwable)
            }
    }

    object PlaceKey : CreationExtras.Key<PlaceUiModel?>

    object PlaceDetailKey : CreationExtras.Key<PlaceDetailUiModel?>
}
