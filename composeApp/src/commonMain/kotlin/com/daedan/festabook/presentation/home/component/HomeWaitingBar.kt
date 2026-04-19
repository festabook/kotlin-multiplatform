package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.home_waiting_bar_action
import festabookkmp.composeapp.generated.resources.home_waiting_bar_format
import festabookkmp.composeapp.generated.resources.ic_arrow_forward_right
import festabookkmp.composeapp.generated.resources.ic_waiting_bar
import festabookkmp.composeapp.generated.resources.move
import festabookkmp.composeapp.generated.resources.my_waiting_enter_now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeWaitingBar(
    order: Int,
    estimatedMinutes: Int,
    status: WaitingStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(FestabookColor.gray800, festabookShapes.radiusFull)
                .clickable(onClick = onClick)
                .shadow(
                    elevation = 4.dp,
                    shape = festabookShapes.radiusFull,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(
                        vertical = festabookSpacing.paddingBody3,
                        horizontal = festabookSpacing.paddingBody4,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(32.dp)
                        .background(FestabookColor.gray700, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_waiting_bar),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = FestabookColor.white,
                )
            }
            Spacer(modifier = Modifier.width(festabookSpacing.paddingBody3))
            val barText = when (status) {
                WaitingStatus.CALLED -> stringResource(Res.string.my_waiting_enter_now)
                else -> stringResource(Res.string.home_waiting_bar_format, order, estimatedMinutes)
            }
            Text(
                text = barText,
                style = FestabookTypography.titleSmall,
                color = FestabookColor.white,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.home_waiting_bar_action),
                style = FestabookTypography.labelMedium,
                color = FestabookColor.white,
            )
            Spacer(modifier = Modifier.width(festabookSpacing.paddingBody2))
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward_right),
                contentDescription = stringResource(Res.string.move),
                tint = FestabookColor.gray300,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
