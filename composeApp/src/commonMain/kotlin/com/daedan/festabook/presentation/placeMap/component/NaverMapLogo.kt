package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.presentation.placeMap.platform.NaverMap

@Composable
expect fun NaverMapLogo(
    naverMap: NaverMap?, // ios는 명령형으로 위치를 조정하기 때문에 ios에서만 사용합니다
    modifier: Modifier = Modifier,
)
