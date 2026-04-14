package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.place_detail_waiting_closed_btn
import festabookkmp.composeapp.generated.resources.place_detail_waiting_estimated_time
import festabookkmp.composeapp.generated.resources.place_detail_waiting_register
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaceDetailBottomBar(
    waiting: WaitingUiState,
    modifier: Modifier = Modifier,
) {
    when (waiting) {
        is WaitingUiState.Active -> {
            Column(modifier = modifier.fillMaxWidth()) {
                HorizontalDivider(color = FestabookColor.gray200)
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(FestabookColor.white)
                            .padding(
                                horizontal = festabookSpacing.paddingScreenGutter,
                                vertical = festabookSpacing.paddingBody3,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text =
                            stringResource(
                                Res.string.place_detail_waiting_estimated_time,
                                waiting.estimatedMinutes,
                            ),
                        style = FestabookTypography.bodySmall,
                        color = FestabookColor.gray500,
                    )

                    Spacer(modifier = Modifier.width(festabookSpacing.paddingBody3))

                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .background(FestabookColor.accentBlue, RoundedCornerShape(8.dp))
                                .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.place_detail_waiting_register),
                            style = FestabookTypography.titleSmall,
                            color = FestabookColor.white,
                        )
                    }
                }
            }
        }

        is WaitingUiState.Closed -> {
            Column(modifier = modifier.fillMaxWidth()) {
                HorizontalDivider(color = FestabookColor.gray200)
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(FestabookColor.white)
                            .padding(
                                horizontal = festabookSpacing.paddingScreenGutter,
                                vertical = festabookSpacing.paddingBody3,
                            ),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(FestabookColor.gray200, RoundedCornerShape(8.dp))
                                .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.place_detail_waiting_closed_btn),
                            style = FestabookTypography.titleSmall,
                            color = FestabookColor.gray500,
                        )
                    }
                }
            }
        }

        else -> {
            Unit
        }
    }
}
