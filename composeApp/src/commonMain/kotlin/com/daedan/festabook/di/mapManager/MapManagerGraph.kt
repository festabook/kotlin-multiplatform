package com.daedan.festabook.di.mapManager

import androidx.compose.ui.unit.Density
import com.daedan.festabook.presentation.placeMap.PlaceMapViewModel
import com.daedan.festabook.presentation.placeMap.mapManager.MapManager
import com.daedan.festabook.presentation.placeMap.model.InitialMapSettingUiModel
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineScope

@DependencyGraph(PlaceMapScope::class)
interface MapManagerGraph {
    val mapManager: MapManager

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides map: NaverMap,
            @Provides settingUiModel: InitialMapSettingUiModel,
            @Provides viewModel: PlaceMapViewModel,
            @Provides initialPadding: Int,
            @Provides scope: CoroutineScope,
            @Provides density: Density,
        ): MapManagerGraph
    }
}
