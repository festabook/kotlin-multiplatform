package com.daedan.festabook.presentation.waiting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.domain.model.WaitingStatus
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
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

@Composable
fun WaitingStatusCard(
    order: Int,
    totalWaitingTeams: Int,
    estimatedWaitTime: Int,
    status: WaitingStatus,
    modifier: Modifier = Modifier,
) {
    val headline = statusHeadline(status, order)
    val orderText = if (status == WaitingStatus.CALLED && order == 1) {
        stringResource(Res.string.my_waiting_enter_now)
    } else {
        stringResource(Res.string.my_waiting_order_format, order)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FestabookColor.black, RoundedCornerShape(16.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = headline,
            style = FestabookTypography.bodyMedium,
            color = FestabookColor.gray400,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = orderText,
            style = FestabookTypography.displayLarge,
            color = FestabookColor.white,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            WaitingInfoItem(
                label = stringResource(Res.string.my_waiting_total_teams_label),
                value = stringResource(Res.string.my_waiting_total_teams_format, totalWaitingTeams),
            )
            WaitingInfoItem(
                label = stringResource(Res.string.my_waiting_estimated_time_label),
                value = stringResource(Res.string.my_waiting_estimated_time_format, estimatedWaitTime),
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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = FestabookTypography.labelSmall,
            color = FestabookColor.gray500,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = FestabookTypography.bodyMedium,
            color = FestabookColor.white,
        )
    }
}

@Composable
private fun statusHeadline(
    status: WaitingStatus,
    order: Int,
): String =
    when {
        status == WaitingStatus.WAITING && order >= 3 ->
            stringResource(Res.string.my_waiting_headline_waiting_far)
        status == WaitingStatus.WAITING && order < 3 ->
            stringResource(Res.string.my_waiting_headline_waiting_near)
        status == WaitingStatus.CALLED ->
            stringResource(Res.string.my_waiting_headline_called)
        else ->
            stringResource(Res.string.my_waiting_headline_waiting_far)
    }
