package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingStatusUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.place_detail_waiting_closed_btn
import festabookkmp.composeapp.generated.resources.place_detail_waiting_estimated_time
import festabookkmp.composeapp.generated.resources.place_detail_waiting_estimated_time_title
import festabookkmp.composeapp.generated.resources.place_detail_waiting_register
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaceDetailBottomBar(
    waiting: WaitingStatusUiState,
    onRegisterWaitingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    clip = false,
                ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(FestabookColor.white)
                    .padding(
                        horizontal = festabookSpacing.paddingScreenGutter,
                        vertical = festabookSpacing.paddingBody2,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlaceDetailBottomBarContent(
                waiting = waiting,
                onRegisterWaitingClick = onRegisterWaitingClick,
            )
        }
    }
}

@Composable
private fun RowScope.PlaceDetailBottomBarContent(
    waiting: WaitingStatusUiState,
    onRegisterWaitingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (waiting) {
        is WaitingStatusUiState.Active -> {
            EstimatedTime(waiting)
            Spacer(modifier = Modifier.width(festabookSpacing.paddingBody3))

            Box(
                modifier =
                    modifier
                        .weight(1f)
                        .background(
                            color = FestabookColor.accentBlue,
                            shape = festabookShapes.radius3,
                        ).padding(vertical = festabookSpacing.paddingBody4)
                        .clickable(onClick = onRegisterWaitingClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.place_detail_waiting_register),
                    style = FestabookTypography.displaySmall,
                    color = FestabookColor.white,
                )
            }
        }

        is WaitingStatusUiState.Closed -> {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = FestabookColor.gray500,
                            shape = festabookShapes.radius3,
                        ).padding(vertical = festabookSpacing.paddingBody4),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.place_detail_waiting_closed_btn),
                    style = FestabookTypography.displaySmall,
                    color = FestabookColor.white,
                )
            }
        }

        else -> {
            Unit
        }
    }
}

@Composable
private fun EstimatedTime(
    waiting: WaitingStatusUiState.Active,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text =
                stringResource(
                    Res.string.place_detail_waiting_estimated_time_title,
                    waiting.estimatedMinutes,
                ),
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray500,
        )
        Text(
            text =
                stringResource(
                    Res.string.place_detail_waiting_estimated_time,
                    waiting.estimatedMinutes,
                ),
            style = FestabookTypography.titleLarge,
            color = FestabookColor.black,
        )
    }
}
