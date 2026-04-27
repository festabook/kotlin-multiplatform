package com.daedan.festabook.presentation.waiting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.placeMap.component.PlaceDetailPreviewContent
import com.daedan.festabook.presentation.placeMap.placeDetail.component.WaitingCancelConfirmDialog
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import com.daedan.festabook.presentation.waiting.MyWaitingUiState
import com.daedan.festabook.presentation.waiting.MyWaitingViewModel
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.explore_back
import festabookkmp.composeapp.generated.resources.ic_arrow_back
import festabookkmp.composeapp.generated.resources.ic_info
import festabookkmp.composeapp.generated.resources.my_waiting_cancel_button
import festabookkmp.composeapp.generated.resources.my_waiting_cancel_confirm_title
import festabookkmp.composeapp.generated.resources.my_waiting_cancel_success
import festabookkmp.composeapp.generated.resources.my_waiting_notice_text
import festabookkmp.composeapp.generated.resources.my_waiting_party_size_format
import festabookkmp.composeapp.generated.resources.my_waiting_party_size_label
import festabookkmp.composeapp.generated.resources.my_waiting_phone_label
import festabookkmp.composeapp.generated.resources.my_waiting_title
import festabookkmp.composeapp.generated.resources.setting_waiting_info_section_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyWaitingRoute(
    viewModel: MyWaitingViewModel,
    onBack: () -> Unit,
    onNavigateToPlaceDetail: (Long) -> Unit,
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
        onNavigateToPlaceDetail = onNavigateToPlaceDetail,
        modifier = modifier,
    )
}

@Composable
fun MyWaitingScreen(
    uiState: MyWaitingUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onCancelWaiting: () -> Unit,
    onNavigateToPlaceDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberNavigationEventState(NavigationEventInfo.None)
    NavigationBackHandler(state = state) { onBack() }

    Scaffold(
        modifier = modifier,
        topBar = { MyWaitingTopBar(onBack = onBack) },
        containerColor = FestabookColor.white,
    ) { innerPadding ->
        when (uiState) {
            is MyWaitingUiState.Loading -> {
                LoadingStateScreen(modifier = Modifier.padding(innerPadding))
            }

            is MyWaitingUiState.Empty -> {
                EmptyStateScreen(modifier = Modifier.padding(innerPadding))
            }

            is MyWaitingUiState.Error -> {
                ErrorStateScreen(modifier = Modifier.padding(innerPadding))
            }

            is MyWaitingUiState.Success -> {
                MyWaitingContent(
                    uiState = uiState,
                    onRefresh = onRefresh,
                    onCancelWaiting = onCancelWaiting,
                    onNavigateToPlaceDetail = onNavigateToPlaceDetail,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyWaitingTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.my_waiting_title),
                style = FestabookTypography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.explore_back),
                    tint = FestabookColor.black,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = FestabookColor.white,
            ),
        modifier = modifier,
    )
}

@Composable
private fun MyWaitingContent(
    uiState: MyWaitingUiState.Success,
    onRefresh: () -> Unit,
    onCancelWaiting: () -> Unit,
    onNavigateToPlaceDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidthDp =
        remember {
            with(density) {
                windowInfo.containerSize.width.toDp()
            }
        }
    var showCancelConfirmDialog by remember { mutableStateOf(false) }

    if (showCancelConfirmDialog) {
        WaitingCancelConfirmDialog(
            title = stringResource(Res.string.my_waiting_cancel_confirm_title),
            onDismissClick = { showCancelConfirmDialog = false },
            onCancelClick = {
                showCancelConfirmDialog = false
                onCancelWaiting()
            },
            onDismissRequest = { showCancelConfirmDialog = false },
        )
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = festabookSpacing.paddingScreenGutter),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(FestabookColor.white)
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            uiState.myWaiting.placeDetail?.let { placeDetail ->
                Box(
                    modifier =
                        Modifier
                            .cardBackground(
                                backgroundColor = FestabookColor.white,
                                shape = festabookShapes.radius4,
                            ).clickable { onNavigateToPlaceDetail(uiState.myWaiting.placeId) },
                ) {
                    PlaceDetailPreviewContent(placeDetail = placeDetail)
                }
            }

            HorizontalDivider(
                color = FestabookColor.gray200,
                thickness = 4.dp,
                modifier =
                    Modifier
                        .requiredWidth(screenWidthDp)
                        .padding(vertical = festabookSpacing.paddingBody5),
            )

            NoShowNoticeCard(
                text = stringResource(Res.string.my_waiting_notice_text),
            )

            HorizontalDivider(
                color = FestabookColor.gray200,
                thickness = 4.dp,
                modifier =
                    Modifier
                        .requiredWidth(screenWidthDp)
                        .padding(vertical = festabookSpacing.paddingBody5),
            )

            WaitingStatusCard(
                order = uiState.myWaiting.order,
                totalWaitingTeams = uiState.myWaiting.totalWaitingTeams,
                estimatedWaitTime = uiState.myWaiting.estimatedWaitTime,
                status = uiState.myWaiting.status,
                isRefreshing = uiState.myWaiting.isRefreshing,
                onRefresh = onRefresh,
            )

            HorizontalDivider(
                color = FestabookColor.gray200,
                thickness = 4.dp,
                modifier =
                    Modifier
                        .requiredWidth(screenWidthDp)
                        .padding(vertical = festabookSpacing.paddingBody5),
            )

            WaitingRegistrationInfo(
                partySize = uiState.myWaiting.partySize,
                phoneNumber = uiState.myWaiting.phoneNumber,
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .requiredWidth(screenWidthDp)
                    .background(FestabookColor.white),
            verticalArrangement = Arrangement.Center,
        ) {
            WaitingCancelButton(
                modifier =
                    Modifier.padding(festabookSpacing.paddingScreenGutter),
                isEnabled = !uiState.myWaiting.isCanceling,
                isCancelling = uiState.myWaiting.isCanceling,
                onClick = { showCancelConfirmDialog = true },
            )
        }
    }
}

@Composable
private fun NoShowNoticeCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .cardBackground()
                .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_info),
            contentDescription = null,
            tint = FestabookColor.gray600,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray600,
        )
    }
}

@Composable
private fun WaitingRegistrationInfo(
    partySize: Int,
    phoneNumber: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.setting_waiting_info_section_title),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = festabookSpacing.paddingBody4),
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

@Composable
private fun WaitingCancelButton(
    isEnabled: Boolean,
    isCancelling: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(festabookShapes.radius2)
                .background(if (isEnabled) FestabookColor.black else FestabookColor.gray300)
                .clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (isCancelling) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = FestabookColor.white,
                strokeWidth = 4.dp,
            )
        } else {
            Text(
                text = stringResource(Res.string.my_waiting_cancel_button),
                style = FestabookTypography.displayMedium,
                color = FestabookColor.white,
            )
        }
    }
}
