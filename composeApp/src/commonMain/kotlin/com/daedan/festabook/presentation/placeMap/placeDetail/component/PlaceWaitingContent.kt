package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.common.component.SkeletonBox
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingTeamUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.place_detail_real_time_waiting
import festabookkmp.composeapp.generated.resources.place_detail_waiting_current_teams
import festabookkmp.composeapp.generated.resources.place_detail_waiting_inactive
import festabookkmp.composeapp.generated.resources.place_detail_waiting_teams_count
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaceWaitingContent(
    waiting: WaitingTeamUiState,
    onRefresh: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    LaunchedEffect(waiting) {
        when (waiting) {
            is WaitingTeamUiState.Error -> {
                currentOnShowErrorSnackbar(waiting.throwable)
            }

            else -> {
                Unit
            }
        }
    }
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = festabookSpacing.paddingBody5),
    ) {
        HorizontalDivider(thickness = 4.dp, color = FestabookColor.gray200)

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = festabookSpacing.paddingScreenGutter,
                        vertical = festabookSpacing.paddingBody5,
                    ),
        ) {
            Text(
                text = stringResource(Res.string.place_detail_real_time_waiting),
                style = FestabookTypography.titleSmall,
            )

            when (waiting) {
                is WaitingTeamUiState.Loading -> {
                    SkeletonBox(
                        modifier =
                            Modifier
                                .padding(top = festabookSpacing.paddingBody2)
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(festabookShapes.radiusFull),
                    )
                }

                is WaitingTeamUiState.InActive -> {
                    WaitingNotSupportedContent(
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
                    )
                }

                else -> {
                    CurrentWaitingTeams(
                        waiting = waiting,
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
                        onRefresh = onRefresh,
                    )
                }
            }
        }
        HorizontalDivider(thickness = 4.dp, color = FestabookColor.gray200)
    }
}

@Composable
private fun WaitingNotSupportedContent(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(FestabookColor.gray500, festabookShapes.radiusFull)
                .padding(
                    horizontal = 24.dp,
                    vertical = 18.dp,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.place_detail_waiting_inactive),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.white,
        )
    }
}

@Composable
private fun CurrentWaitingTeams(
    waiting: WaitingTeamUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(FestabookColor.black, festabookShapes.radiusFull)
                .padding(
                    horizontal = 24.dp,
                    vertical = festabookSpacing.paddingBody4,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.place_detail_waiting_current_teams),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        when (waiting) {
            is WaitingTeamUiState.Success -> {
                Text(
                    text =
                        stringResource(
                            Res.string.place_detail_waiting_teams_count,
                            waiting.totalTeams,
                        ),
                    style = FestabookTypography.displayMedium,
                    color = FestabookColor.white,
                )
            }

            is WaitingTeamUiState.Refresh -> {
                Text(
                    text =
                        stringResource(
                            Res.string.place_detail_waiting_teams_count,
                            waiting.totalTeams,
                        ),
                    style = FestabookTypography.displayMedium,
                    color = FestabookColor.white,
                )
            }

            else -> {
                Text(
                    text = "-",
                    style = FestabookTypography.displayMedium,
                    color = FestabookColor.white,
                )
            }
        }

        Spacer(modifier = Modifier.width(festabookSpacing.paddingBody4))

        RefreshButton(
            onClick = onRefresh,
            isRefreshing = waiting is WaitingTeamUiState.Refresh,
        )
    }
}

@Composable
private fun RefreshButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
) {
    val rotation = remember { Animatable(0f) }
    val durationMillis = 500

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            rotation.snapTo(rotation.value % 360f)
            rotation.animateTo(
                targetValue = 360f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
            )
        } else {
            val remainder = rotation.value % 360f
            if (remainder > 0f) {
                val remainingFraction = (360f - remainder) / 360f
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec =
                        tween(
                            durationMillis = (remainingFraction * durationMillis).toInt().coerceAtLeast(1),
                            easing = LinearEasing,
                        ),
                )
            }
            rotation.snapTo(0f)
        }
    }

    Box(
        modifier =
            modifier
                .background(
                    color = FestabookColor.white,
                    shape = festabookShapes.radiusFull,
                ).clickable(
                    onClick = onClick,
                ).padding(festabookSpacing.paddingBody1),
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = FestabookColor.black,
            modifier = Modifier.size(20.dp).rotate(rotation.value),
        )
    }
}
