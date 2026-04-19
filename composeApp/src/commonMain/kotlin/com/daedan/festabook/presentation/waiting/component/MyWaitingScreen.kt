package com.daedan.festabook.presentation.waiting.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.waiting.MyWaitingPlaceUiModel
import com.daedan.festabook.presentation.waiting.MyWaitingUiState
import com.daedan.festabook.presentation.waiting.MyWaitingViewModel
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.explore_back
import festabookkmp.composeapp.generated.resources.ic_arrow_back
import festabookkmp.composeapp.generated.resources.my_waiting_cancel_button
import festabookkmp.composeapp.generated.resources.my_waiting_cancel_success
import festabookkmp.composeapp.generated.resources.my_waiting_notice_text
import festabookkmp.composeapp.generated.resources.my_waiting_party_size_format
import festabookkmp.composeapp.generated.resources.my_waiting_party_size_label
import festabookkmp.composeapp.generated.resources.my_waiting_phone_label
import festabookkmp.composeapp.generated.resources.my_waiting_title
import festabookkmp.composeapp.generated.resources.refresh
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyWaitingRoute(
    viewModel: MyWaitingViewModel,
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    val cancelSuccessText = stringResource(Res.string.my_waiting_cancel_success)

    ObserveAsEvents(flow = viewModel.cancelSuccessEvent) {
        onShowSnackbar(cancelSuccessText)
        onBack()
    }

    ObserveAsEvents(flow = viewModel.errorEvent) {
        currentOnShowErrorSnackbar(it)
    }

    MyWaitingScreen(
        uiState = uiState,
        onBack = onBack,
        onRefresh = viewModel::refresh,
        onCancelWaiting = viewModel::cancelWaiting,
        modifier = modifier,
    )
}

@Composable
fun MyWaitingScreen(
    uiState: MyWaitingUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onCancelWaiting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberNavigationEventState(NavigationEventInfo.None)
    NavigationBackHandler(state = state) { onBack() }

    val isRefreshing = (uiState as? MyWaitingUiState.Success)?.isRefreshing ?: false
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        label = "refresh_rotation",
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            MyWaitingTopBar(
                onBack = onBack,
                onRefresh = onRefresh,
                rotationAngle = rotationAngle,
                showRefresh = uiState is MyWaitingUiState.Success,
            )
        },
        containerColor = FestabookColor.white,
    ) { innerPadding ->
        when (uiState) {
            is MyWaitingUiState.Loading -> LoadingStateScreen(modifier = Modifier.padding(innerPadding))
            is MyWaitingUiState.Empty -> EmptyStateScreen(modifier = Modifier.padding(innerPadding))
            is MyWaitingUiState.Error -> ErrorStateScreen(modifier = Modifier.padding(innerPadding))
            is MyWaitingUiState.Success -> {
                MyWaitingContent(
                    uiState = uiState,
                    onCancelWaiting = onCancelWaiting,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
private fun MyWaitingTopBar(
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    rotationAngle: Float,
    showRefresh: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_arrow_back),
                contentDescription = stringResource(Res.string.explore_back),
            )
        }
        Text(
            text = stringResource(Res.string.my_waiting_title),
            style = FestabookTypography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        if (showRefresh) {
            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(Res.string.refresh),
                    modifier = Modifier.rotate(rotationAngle),
                )
            }
        }
    }
}

@Composable
private fun MyWaitingContent(
    uiState: MyWaitingUiState.Success,
    onCancelWaiting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        uiState.place?.let { place ->
            PlaceInfoCard(place = place)
            Spacer(modifier = Modifier.height(16.dp))
        }

        WaitingStatusCard(
            order = uiState.order,
            totalWaitingTeams = uiState.totalWaitingTeams,
            estimatedWaitTime = uiState.estimatedWaitTime,
            status = uiState.status,
        )

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = FestabookColor.gray200)
        Spacer(modifier = Modifier.height(16.dp))

        WaitingRegistrationInfo(
            partySize = uiState.partySize,
            phoneNumber = uiState.phoneNumber,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.my_waiting_notice_text),
            style = FestabookTypography.labelSmall,
            color = FestabookColor.gray500,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCancelWaiting,
            enabled = !uiState.isCanceling,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = FestabookColor.gray200,
                contentColor = FestabookColor.gray700,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.my_waiting_cancel_button),
                style = FestabookTypography.bodyMedium,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PlaceInfoCard(
    place: MyWaitingPlaceUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(FestabookColor.gray100, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = place.title,
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.black,
            )
            place.location?.let { location ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = location,
                    style = FestabookTypography.labelSmall,
                    color = FestabookColor.gray500,
                )
            }
            place.operatingTime?.let { time ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = time,
                    style = FestabookTypography.labelSmall,
                    color = FestabookColor.gray500,
                )
            }
        }
    }
}

@Composable
private fun WaitingRegistrationInfo(
    partySize: Int,
    phoneNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.my_waiting_party_size_label),
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.gray500,
            )
            Text(
                text = stringResource(Res.string.my_waiting_party_size_format, partySize),
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.black,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.my_waiting_phone_label),
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.gray500,
            )
            Text(
                text = phoneNumber,
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.black,
            )
        }
    }
}
