package com.daedan.festabook.presentation.placeMap.intent.handler

import androidx.compose.ui.unit.Density
import com.daedan.festabook.di.mapManager.MapManagerGraph
import com.daedan.festabook.domain.model.TimeTag
import com.daedan.festabook.presentation.placeMap.PlaceMapViewModel
import com.daedan.festabook.presentation.placeMap.intent.event.SelectEvent
import com.daedan.festabook.presentation.placeMap.intent.sideEffect.MapControlSideEffect
import com.daedan.festabook.presentation.placeMap.intent.state.LoadState
import com.daedan.festabook.presentation.placeMap.intent.state.MapDelegate
import com.daedan.festabook.presentation.placeMap.intent.state.MapManagerDelegate
import com.daedan.festabook.presentation.placeMap.mapManager.MapManager
import com.daedan.festabook.presentation.placeMap.platform.LocationSource
import dev.zacsweers.metro.createGraphFactory
import kotlinx.coroutines.CoroutineScope

class MapControlSideEffectHandler(
    private val initialPadding: Int,
//    private val logger: DefaultFirebaseLogger,
    private val locationSource: LocationSource,
    private val viewModel: PlaceMapViewModel,
    private val mapDelegate: MapDelegate,
    private val mapManagerDelegate: MapManagerDelegate,
    private val scope: CoroutineScope,
    private val density: Density,
) : SideEffectHandler<MapControlSideEffect> {
    private val uiState get() = viewModel.uiState.value
    private val mapManager: MapManager? get() = mapManagerDelegate.value

    override suspend operator fun invoke(event: MapControlSideEffect) {
        when (event) {
            is MapControlSideEffect.InitMap -> {
                val naverMap = mapDelegate.await()
                naverMap.addOnLocationChangeListener {
//                    logger.log(
//                        CurrentLocationChecked(
//                            baseLogData = logger.getBaseLogData(),
//                        ),
//                    )
                }
                naverMap.locationSource = locationSource
            }

            is MapControlSideEffect.InitMapManager -> {
                val naverMap = mapDelegate.await()
                if (mapManager == null) {
                    val graph =
                        createGraphFactory<MapManagerGraph.Factory>().create(
                            naverMap,
                            event.initialMapSetting,
                            viewModel,
                            initialPadding,
                            scope,
                            density,
                        )
                    mapManagerDelegate.init(graph.mapManager)
                    mapManager?.setupBackToInitialPosition { isExceededMaxLength ->
                        viewModel.onPlaceMapEvent(
                            SelectEvent.ExceededMaxLength(isExceededMaxLength),
                        )
                    }
                }
            }

            is MapControlSideEffect.BackToInitialPosition -> {
                mapManager?.moveToPosition()
            }

            is MapControlSideEffect.SetMarkerByTimeTag -> {
                if (event.isInitial) {
                    mapManager?.setupMarker(event.placeGeographies)
                }

                when (val selectedTimeTag = event.selectedTimeTag) {
                    is LoadState.Success -> {
                        mapManager?.filterMarkersByTimeTag(
                            selectedTimeTag.value.timeTagId,
                        )
                    }

                    is LoadState.Empty -> {
                        mapManager?.filterMarkersByTimeTag(TimeTag.EMTPY_TIME_TAG_ID)
                    }

                    else -> {
                        Unit
                    }
                }
            }

            is MapControlSideEffect.FilterMapByCategory -> {
                val selectedCategories = event.selectedCategories
                if (selectedCategories.isEmpty()) {
                    mapManager?.clearFilter()
                } else {
                    mapManager?.filterMarkersByCategories(selectedCategories)
                }
            }

            is MapControlSideEffect.SelectMarker -> {
                when (val place = event.placeDetail) {
                    is LoadState.Success -> {
                        mapManager?.selectMarker(place.value.place.id)

                        val currentTimeTag = uiState.selectedTimeTag
                        val timeTagName =
                            if (currentTimeTag is LoadState.Success) {
                                currentTimeTag.value.name
                            } else {
                                "undefined"
                            }
//                        logger.log(
//                            PlaceMarkerClick(
//                                baseLogData = logger.getBaseLogData(),
//                                placeId = place.value.place.id,
//                                timeTagName = timeTagName,
//                                category = place.value.place.category.name,
//                            ),
//                        )
                    }

                    else -> {
                        Unit
                    }
                }
            }

            is MapControlSideEffect.UnselectMarker -> {
                mapManager?.unselectMarker()
            }
        }
    }
}
