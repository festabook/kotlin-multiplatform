package com.daedan.festabook.presentation.waiting.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.my_waiting_enter_now
import festabookkmp.composeapp.generated.resources.my_waiting_estimated_time_format
import festabookkmp.composeapp.generated.resources.my_waiting_estimated_time_label
import festabookkmp.composeapp.generated.resources.my_waiting_headline_called
import festabookkmp.composeapp.generated.resources.my_waiting_headline_waiting_far
import festabookkmp.composeapp.generated.resources.my_waiting_headline_waiting_near
import festabookkmp.composeapp.generated.resources.my_waiting_order_format
import festabookkmp.composeapp.generated.resources.my_waiting_total_teams_format
import festabookkmp.composeapp.generated.resources.my_waiting_total_teams_label
import org.jetbrains.compose.resources.stringResource

private const val WAITING_NEAR_THRESHOLD = 3

@Composable
fun WaitingStatusCard(
    order: Int,
    totalWaitingTeams: Int,
    estimatedWaitTime: Int,
    status: WaitingStatus,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val headline = statusHeadline(status, order)
    val showEnterNow = status == WaitingStatus.CALLED

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(FestabookColor.black, festabookShapes.radius4)
                .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = headline,
                style = FestabookTypography.displaySmall,
                color = FestabookColor.white,
                modifier = Modifier.weight(1f),
            )
            RefreshButton(
                onClick = onRefresh,
                isRefreshing = isRefreshing,
            )
        }

        Spacer(Modifier.height(festabookSpacing.paddingBody5))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (showEnterNow) {
                Text(
                    text = stringResource(Res.string.my_waiting_enter_now),
                    style = FestabookTypography.displayLarge,
                    color = FestabookColor.white,
                )
            } else {
                val orderFormatted = stringResource(Res.string.my_waiting_order_format)
                val numberText = order.toString()
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.offset(y = (-4).dp),
                ) {
                    Text(
                        text = numberText,
                        style = FestabookTypography.displayLarge.copy(fontSize = 48.sp),
                        color = FestabookColor.white,
                    )
                    Text(
                        text = orderFormatted,
                        style = FestabookTypography.titleLarge,
                        color = FestabookColor.white,
                        modifier = Modifier.padding(start = 2.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(festabookSpacing.paddingBody3))
        HorizontalDivider(color = FestabookColor.white)
        Spacer(Modifier.height(festabookSpacing.paddingBody2))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WaitingInfoItem(
                label = stringResource(Res.string.my_waiting_total_teams_label),
                value = stringResource(Res.string.my_waiting_total_teams_format, totalWaitingTeams),
                modifier =
                    Modifier
                        .padding(top = festabookSpacing.paddingBody1)
                        .weight(1f),
            )
            VerticalDivider(color = FestabookColor.white)
            WaitingInfoItem(
                label = stringResource(Res.string.my_waiting_estimated_time_label),
                value =
                    stringResource(
                        Res.string.my_waiting_estimated_time_format,
                        estimatedWaitTime,
                    ),
                modifier =
                    Modifier
                        .padding(
                            top = festabookSpacing.paddingBody1,
                            start = festabookSpacing.paddingBody2,
                        ).weight(1f),
            )
        }
    }
}

@Composable
private fun WaitingInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
    ) {
        Text(
            text = label,
            style = FestabookTypography.bodySmall,
            color = FestabookColor.white,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = FestabookTypography.displaySmall,
            color = FestabookColor.white,
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
                animationSpec = infiniteRepeatable(
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
                    animationSpec = tween(
                        durationMillis = (remainingFraction * durationMillis).toInt()
                            .coerceAtLeast(1),
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

@Composable
private fun statusHeadline(
    status: WaitingStatus,
    order: Int,
): String =
    when (status) {
        WaitingStatus.WAITING if order > WAITING_NEAR_THRESHOLD -> {
            stringResource(Res.string.my_waiting_headline_waiting_far)
        }

        WaitingStatus.WAITING if order <= WAITING_NEAR_THRESHOLD -> {
            stringResource(Res.string.my_waiting_headline_waiting_near)
        }

        WaitingStatus.CALLED -> {
            stringResource(Res.string.my_waiting_headline_called)
        }

        else -> {
            stringResource(Res.string.my_waiting_headline_waiting_far)
        }
    }
