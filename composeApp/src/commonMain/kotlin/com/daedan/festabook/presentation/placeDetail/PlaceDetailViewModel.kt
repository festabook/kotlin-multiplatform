package com.daedan.festabook.presentation.placeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.daedan.festabook.domain.repository.PlaceDetailRepository
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.placeDetail.model.ImageUiModel
import com.daedan.festabook.presentation.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.placeDetail.model.PlaceDetailUiState
import com.daedan.festabook.presentation.placeDetail.model.toUiModel
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AssistedInject
class PlaceDetailViewModel(
    private val placeDetailRepository: PlaceDetailRepository,
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
                }.onFailure {
                    _placeDetail.value = PlaceDetailUiState.Error(it)
                }
        }
    }

    fun toggleNoticeExpanded(notice: NoticeUiModel) {
        val currentState = _placeDetail.value
        if (currentState !is PlaceDetailUiState.Success) return
        _placeDetail.value =
            currentState.copy(
                placeDetail =
                    currentState.placeDetail.copy(
                        notices =
                            currentState.placeDetail.notices.map {
                                if (notice.id == it.id) {
                                    it.copy(isExpanded = !it.isExpanded)
                                } else {
                                    it
                                }
                            },
                    ),
            )
    }

    object PlaceKey : CreationExtras.Key<PlaceUiModel?>

    object PlaceDetailKey : CreationExtras.Key<PlaceDetailUiModel?>

//    companion object {
//        fun factory(
//            factory: Factory,
//            place: PlaceUiModel?,
//            receivedPlaceDetail: PlaceDetailUiModel?,
//        ) = viewModelFactory {
//            initializer {
//                factory.create(place, receivedPlaceDetail)
//            }
//        }
//    }
}
