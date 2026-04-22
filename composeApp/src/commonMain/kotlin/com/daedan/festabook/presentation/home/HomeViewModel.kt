package com.daedan.festabook.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.repository.FestivalRepository
import com.daedan.festabook.presentation.home.model.toUiModel
import com.daedan.festabook.domain.repository.MyWaitingRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class)
@ViewModelKey(HomeViewModel::class)
@Inject
class HomeViewModel(
    private val festivalRepository: FestivalRepository,
    private val myWaitingRepository: MyWaitingRepository,
) : ViewModel() {
    private val _festivalUiState = MutableStateFlow<FestivalUiState>(FestivalUiState.Loading)
    val festivalUiState: StateFlow<FestivalUiState> = _festivalUiState.asStateFlow()

    private val _lineupUiState = MutableStateFlow<LineupUiState>(LineupUiState.Loading)
    val lineupUiState: StateFlow<LineupUiState> = _lineupUiState.asStateFlow()

    private val _navigateToScheduleEvent =
        MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val navigateToScheduleEvent: SharedFlow<Unit> = _navigateToScheduleEvent.asSharedFlow()

    private val _waitingBarUiState = MutableStateFlow<WaitingBarUiState>(WaitingBarUiState.Hidden)
    val waitingBarUiState: StateFlow<WaitingBarUiState> = _waitingBarUiState.asStateFlow()

    private val _navigateToMyWaitingEvent =
        MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val navigateToMyWaitingEvent: SharedFlow<Unit> = _navigateToMyWaitingEvent.asSharedFlow()

    init {
        loadFestival()
        loadLineup()
    }

    fun loadFestival() {
        viewModelScope.launch {
            _festivalUiState.value = FestivalUiState.Loading

            val result = festivalRepository.getFestivalInfo()
            result
                .onSuccess { organization ->
                    _festivalUiState.value = FestivalUiState.Success(organization.toUiModel())
                }.onFailure {
                    _festivalUiState.value = FestivalUiState.Error(it)
                }
        }
    }

    fun navigateToScheduleClick() {
        _navigateToScheduleEvent.tryEmit(Unit)
    }

    fun navigateToMyWaitingClick() {
        _navigateToMyWaitingEvent.tryEmit(Unit)
    }

    fun loadWaitingBar() {
        viewModelScope.launch {
            myWaitingRepository
                .getMyWaiting()
                .onSuccess { result ->
                    _waitingBarUiState.value = result?.let {
                        WaitingBarUiState.Visible(
                            order = it.waitingOrder,
                            estimatedWaitTime = it.estimatedWaitTime,
                            status = it.waitingStatus,
                        )
                    } ?: WaitingBarUiState.Hidden
                }.onFailure { t ->
                    Napier.e("웨이팅 바 로딩 실패", t)
                    _waitingBarUiState.value = WaitingBarUiState.Hidden
                }
        }
    }

    private fun loadLineup() {
        viewModelScope.launch {
            _lineupUiState.value = LineupUiState.Loading

            val result = festivalRepository.getLineUpGroupByDate()
            result
                .onSuccess { lineups ->
                    val lineupItems = lineups.toUiModel().getLineupItems()
                    _lineupUiState.value = LineupUiState.Success(lineupItems)
                }.onFailure {
                    _lineupUiState.value = LineupUiState.Error(it)
                }
        }
    }
}
