package com.daedan.festabook.presentation.placeMap.mapManager

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.presentation.placeMap.listener.MapClickListener
import com.daedan.festabook.presentation.placeMap.model.CoordinateUiModel
import com.daedan.festabook.presentation.placeMap.model.InitialMapSettingUiModel
import com.daedan.festabook.presentation.placeMap.model.toLatLng
import com.daedan.festabook.presentation.placeMap.platform.LatLng
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import com.daedan.festabook.presentation.placeMap.platform.PolygonOverlay
import dev.zacsweers.metro.Inject

@Inject
class MapManager(
    private val map: NaverMap,
    private val density: Density,
    private val mapClickListener: MapClickListener,
    private val settingUiModel: InitialMapSettingUiModel,
    private val cameraManager: MapCameraManager,
    private val filterManager: MapFilterManager,
    private val markerManager: MapMarkerManager,
) : MapCameraManager by cameraManager,
    MapMarkerManager by markerManager,
    MapFilterManager by filterManager {
    init {
        map.apply {
            isIndoorEnabled = true
            symbolScale = SYMBOL_SIZE_WEIGHT
            customStyleId = BuildKonfig.NAVER_MAP_STYLE_ID
            uiSettings.isZoomControlEnabled = false
            uiSettings.isScaleBarEnabled = false
            cameraManager.setCameraInitialPosition()
            setInitialPolygon(settingUiModel.border)
            setContentPaddingBottom(INITIAL_PADDING)
            setLogoMarginBottom()

            setOnMapClickListener {
                markerManager.unselectMarker()
                mapClickListener.onMapClickListener()
            }
        }
    }

    private fun setContentPaddingBottom(height: Dp) {
        map.setContentPadding(
            left = 0.dp,
            top = 0.dp,
            right = 0.dp,
            bottom = height,
            density = density,
            animate = true,
        )
    }

    private fun setLogoMarginBottom() {
        map.uiSettings.setLogoMargin(
            16.dp.toPx(),
            0,
            0,
            Int.MAX_VALUE,
        )
    }

    // 생성자로 입력받은 초기 위치 경계를 설정합니다
    private fun setInitialPolygon(border: List<CoordinateUiModel>) {
        PolygonOverlay(
            coords = EDGE_COORS,
            holes =
                listOf(
                    border.map {
                        it.toLatLng()
                    },
                ),
        ).apply {
            color = OVERLAY_COLOR_ID
            outlineWidth = OVERLAY_OUTLINE_STROKE_WIDTH
            map = this@MapManager.map
        }
    }

    private fun Dp.toPx() = with(density) { toPx() }.toInt()

    companion object {
        private const val OVERLAY_OUTLINE_STROKE_WIDTH = 4
        private const val SYMBOL_SIZE_WEIGHT = 0.8

        private const val OVERLAY_COLOR_ID = 0x4D1B1B1B

        private val INITIAL_PADDING = 254.dp

        // 대한민국 전체를 덮는 오버레이 좌표입니다
        private val EDGE_COORS =
            listOf(
                LatLng(39.2163345, 123.5125660),
                LatLng(39.2163345, 130.5440844),
                LatLng(32.8709533, 130.5440844),
                LatLng(32.8709533, 123.5125660),
            )
    }
}
