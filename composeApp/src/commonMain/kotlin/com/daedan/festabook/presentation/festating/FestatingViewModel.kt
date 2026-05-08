package com.daedan.festabook.presentation.festating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daedan.festabook.domain.repository.FestivalRepository
import com.daedan.festabook.presentation.home.model.toUiModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class)
@ViewModelKey(FestatingViewModel::class)
@Inject
class FestatingViewModel(
    private val festivalRepository: FestivalRepository,
) : ViewModel() {
    private val _festatingUiState = MutableStateFlow<FestatingUiState>(FestatingUiState.Loading)
    val festatingUiState: StateFlow<FestatingUiState> = _festatingUiState.asStateFlow()

    init {
        loadFestating()
    }

    private fun loadFestating() {
        viewModelScope.launch {
            _festatingUiState.value = FestatingUiState.Loading

            val result = festivalRepository.getFestating()
            result
                .onSuccess {
                    _festatingUiState.value = FestatingUiState.Success(it.toUiModel())
                }.onFailure {
                    _festatingUiState.value = FestatingUiState.Error(it)
                }
        }
    }
}
