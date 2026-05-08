package com.daedan.festabook.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.di.viewmodel.ViewModelKey
import com.daedan.festabook.domain.repository.FestivalRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

@ContributesIntoMap(AppScope::class)
@ViewModelKey(MainViewModel::class)
@Inject
class MainViewModel(
    festivalRepository: FestivalRepository,
) : ViewModel() {
    private val _backPressEvent: MutableSharedFlow<Boolean> =
        MutableSharedFlow(extraBufferCapacity = 1)
    val backPressEvent: SharedFlow<Boolean> = _backPressEvent.asSharedFlow()

    private val _navigateNewsEvent: Channel<Unit> = Channel(capacity = 1)
    val navigateNewsEvent = _navigateNewsEvent.receiveAsFlow()

    private val _isFirstVisit = MutableStateFlow(false)
    val isFirstVisit: StateFlow<Boolean> = _isFirstVisit.asStateFlow()

    private var lastBackPressedTime: Long = 0

    init {
        viewModelScope.launch {
            val isFirstVisit = festivalRepository.getIsFirstVisit().firstOrNull()
            isFirstVisit?.let {
                _isFirstVisit.value = it
            } ?: run {
                // TODO 로그
            }
        }
    }

    fun navigateToNews() {
        _navigateNewsEvent.trySend(Unit)
    }

    fun declineAlert() {
        _isFirstVisit.value = false
    }

    fun onBackPressed() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastBackPressedTime < BACK_PRESS_INTERVAL) {
            _backPressEvent.tryEmit(true)
        } else {
            lastBackPressedTime = currentTime
            _backPressEvent.tryEmit(false)
        }
    }

    companion object {
        private const val BACK_PRESS_INTERVAL: Long = 2000L
    }
}
