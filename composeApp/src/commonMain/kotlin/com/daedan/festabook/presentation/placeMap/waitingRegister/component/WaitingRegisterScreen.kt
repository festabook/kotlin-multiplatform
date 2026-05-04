package com.daedan.festabook.presentation.placeMap.waitingRegister.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.logging.ScreenViewLogger
import com.daedan.festabook.logging.logClick
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.PermissionState
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.placeMap.component.PlaceDetailPreviewContent
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.WaitingRegisterViewModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingPlaceSummaryUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiState
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.component.NotificationPermissionDialog
import com.daedan.festabook.presentation.setting.component.platform.rememberNotificationPermissionManager
import com.daedan.festabook.presentation.setting.component.platform.rememberOpenAppSettings
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.btn_back_to_previous
import festabookkmp.composeapp.generated.resources.content_description_waiting_party_size_decrease
import festabookkmp.composeapp.generated.resources.content_description_waiting_party_size_increase
import festabookkmp.composeapp.generated.resources.content_description_waiting_register_back
import festabookkmp.composeapp.generated.resources.waiting_register_agreement_service
import festabookkmp.composeapp.generated.resources.waiting_register_agreement_title
import festabookkmp.composeapp.generated.resources.waiting_register_party_size_limit_notice
import festabookkmp.composeapp.generated.resources.waiting_register_party_size_title
import festabookkmp.composeapp.generated.resources.waiting_register_submit
import festabookkmp.composeapp.generated.resources.waiting_register_success
import festabookkmp.composeapp.generated.resources.waiting_register_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val PRIVACY_AGREEMENT_URL =
    "https://www.notion.so/2026-04-01-335a540dc0b780fa9897e0a3bdf45bae"

@Composable
fun WaitingRegisterRoute(
    viewModel: WaitingRegisterViewModel,
    settingViewModel: SettingViewModel,
    notificationPermissionManagerFactory: NotificationPermissionManager.Factory,
    onBackToPreviousClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onNavigateToPhoneRegistration: () -> Unit,
    onNavigateToMyWaiting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenViewLogger("WaitingRegisterScreen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isNotificationAllowed by settingViewModel.isAllowed.collectAsStateWithLifecycle()
    val successMessage = stringResource(Res.string.waiting_register_success)
    var showConfirmBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    val onOpenAppSettings = rememberOpenAppSettings()
    val currentOnNavigateToPhoneRegistration by rememberUpdatedState(onNavigateToPhoneRegistration)

    val notificationPermissionManager =
        rememberNotificationPermissionManager(
            notificationPermissionManagerFactory = notificationPermissionManagerFactory,
            onPermissionGrant = {
                showConfirmBottomSheet = false
                settingViewModel.saveNotificationId()
                viewModel.submitWaitingRegister()
            },
            onPermissionDeny = {},
        )

    ObserveAsEvents(viewModel.registerSuccessEvent) {
        onShowSnackbar(successMessage)
        onNavigateToMyWaiting()
    }
    ObserveAsEvents(viewModel.registerFailureEvent) { throwable ->
        onShowErrorSnackbar(throwable)
    }

    ObserveAsEvents(flow = settingViewModel.permissionCheckEvent) {
        val permission = notificationPermissionManager.checkPermission()

        when (permission) {
            PermissionState.GRANTED -> {
                showConfirmBottomSheet = false
                settingViewModel.saveNotificationId()
                viewModel.submitWaitingRegister()
            }

            PermissionState.NEED_RATIONALE -> {
                onOpenAppSettings()
            }

            PermissionState.DENIED -> {
                notificationPermissionManager.requestPermission()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is WaitingRegisterUiState.NeedsPhoneRegistration) {
            currentOnNavigateToPhoneRegistration()
        }
    }

    WaitingRegisterScreen(
        uiState = uiState,
        onBackToPreviousClick = onBackToPreviousClick,
        onIncreasePartySize = viewModel::increasePartySize,
        onDecreasePartySize = viewModel::decreasePartySize,
        onToggleServiceAgreement = viewModel::toggleServiceAgreement,
        onSubmitClick = { showConfirmBottomSheet = true },
        onShowErrorSnackbar = onShowErrorSnackbar,
        modifier = modifier,
    )

    if (showConfirmBottomSheet) {
        WaitingRegisterConfirmBottomSheet(
            onConfirm = {
                if (isNotificationAllowed) {
                    showConfirmBottomSheet = false
                    viewModel.submitWaitingRegister()
                } else {
                    showPermissionDialog = true
                }
            },
            onDismiss = { showConfirmBottomSheet = false },
        )
    }

    if (showPermissionDialog) {
        NotificationPermissionDialog(
            onConfirm = {
                showPermissionDialog = false
                settingViewModel.notificationAllowClick()
            },
        )
    }
}

@Composable
fun WaitingRegisterScreen(
    uiState: WaitingRegisterUiState,
    onBackToPreviousClick: () -> Unit,
    onIncreasePartySize: () -> Unit,
    onDecreasePartySize: () -> Unit,
    onToggleServiceAgreement: () -> Unit,
    onSubmitClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    val state = rememberNavigationEventState(NavigationEventInfo.None)
    val isSubmitting =
        (uiState as? WaitingRegisterUiState.Success)?.waitingRegister?.isSubmitting ?: false

    val loggedOnBackToPreviousClick = logClick(identifier = "back", screenName = "WaitingRegisterScreen", onClick = onBackToPreviousClick)
    val loggedOnToggleServiceAgreement =
        logClick(identifier = "toggle_service_agreement", screenName = "WaitingRegisterScreen", onClick = onToggleServiceAgreement)
    val partySize = (uiState as? WaitingRegisterUiState.Success)?.waitingRegister?.partySize
    val loggedOnSubmitClick = logClick(
        identifier = "submit_waiting",
        screenName = "WaitingRegisterScreen",
        extraParam = if (partySize != null) mapOf("party_size" to partySize.toString()) else emptyMap(),
        onClick = onSubmitClick,
    )

    NavigationBackHandler(
        state = state,
        isBackEnabled = !isSubmitting,
    ) {
        onBackToPreviousClick()
    }

    LaunchedEffect(uiState) {
        if (uiState is WaitingRegisterUiState.Error) {
            currentOnShowErrorSnackbar(uiState.throwable)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        WaitingRegisterTopBar(onBackClick = loggedOnBackToPreviousClick)

        when (uiState) {
            is WaitingRegisterUiState.Loading,
            is WaitingRegisterUiState.NeedsPhoneRegistration,
            -> {
                Unit
            }

            is WaitingRegisterUiState.Error -> {
                Box(modifier = Modifier.weight(1f)) {
                    ErrorStateScreen()
                }
            }

            is WaitingRegisterUiState.Success -> {
                val waitingRegister = uiState.waitingRegister
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        WaitingPlaceSummaryCard(
                            summary = waitingRegister.placeSummary,
                            modifier = Modifier.padding(top = 40.dp),
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 24.dp),
                            thickness = 4.dp,
                            color = FestabookColor.gray200,
                        )

                        PartySizeSection(
                            partySize = waitingRegister.partySize,
                            canDecrease = waitingRegister.canDecreasePartySize,
                            canIncrease = waitingRegister.canIncreasePartySize,
                            onDecrease = onDecreasePartySize,
                            onIncrease = onIncreasePartySize,
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 24.dp),
                            thickness = 4.dp,
                            color = FestabookColor.gray200,
                        )

                        AgreementSection(
                            isAgreed = waitingRegister.isServiceAgreed,
                            onToggle = loggedOnToggleServiceAgreement,
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    WaitingRegisterSubmitButton(
                        isEnabled = waitingRegister.canSubmit,
                        isSubmitting = waitingRegister.isSubmitting,
                        onClick = loggedOnSubmitClick,
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

@Composable
private fun WaitingRegisterTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        Image(
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = festabookSpacing.paddingScreenGutter)
                    .size(30.dp)
                    .clickable { onBackClick() },
            painter = painterResource(Res.drawable.btn_back_to_previous),
            contentDescription = stringResource(Res.string.content_description_waiting_register_back),
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(Res.string.waiting_register_title),
            style = FestabookTypography.titleMedium,
        )
    }
}

@Composable
private fun WaitingPlaceSummaryCard(
    summary: WaitingPlaceSummaryUiModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(horizontal = festabookSpacing.paddingScreenGutter)
                .cardBackground(
                    backgroundColor = FestabookColor.white,
                    shape = festabookShapes.radius4,
                ),
    ) {
        PlaceDetailPreviewContent(
            placeDetail =
                PlaceDetailUiModel(
                    place =
                        PlaceUiModel(
                            id = summary.placeId,
                            imageUrl = summary.imageUrl,
                            category = summary.category,
                            title = summary.title,
                            description = summary.description,
                            location = summary.location,
                            isBookmarked = false,
                            timeTagId = emptyList(),
                        ),
                    notices = emptyList(),
                    host = summary.host,
                    startTime = summary.startTime,
                    endTime = summary.endTime,
                    images = emptyList(),
                ),
        )
    }
}

@Composable
private fun PartySizeSection(
    partySize: Int,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.waiting_register_party_size_title),
            style = FestabookTypography.displayMedium,
        )

        Row(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            PartySizeButton(
                label = "-",
                enabled = canDecrease,
                onClick = onDecrease,
                contentDescription = stringResource(Res.string.content_description_waiting_party_size_decrease),
            )

            Text(
                text = partySize.toString(),
                style = FestabookTypography.titleLarge.copy(fontSize = 40.sp),
            )

            PartySizeButton(
                label = "+",
                enabled = canIncrease,
                onClick = onIncrease,
                contentDescription = stringResource(Res.string.content_description_waiting_party_size_increase),
            )
        }

        Text(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
            text = stringResource(Res.string.waiting_register_party_size_limit_notice),
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray500,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PartySizeButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(44.dp)
                .border(
                    width = 1.dp,
                    color = if (enabled) FestabookColor.gray400 else FestabookColor.gray200,
                    shape = CircleShape,
                ).clip(CircleShape)
                .clickable(enabled = enabled, onClickLabel = contentDescription) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 28.sp),
            color = if (enabled) FestabookColor.gray500 else FestabookColor.gray300,
        )
    }
}

@Composable
private fun AgreementSection(
    isAgreed: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = festabookSpacing.paddingScreenGutter),
    ) {
        Text(
            text = stringResource(Res.string.waiting_register_agreement_title),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )

        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody1))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.clip(festabookShapes.radius1),
                checked = isAgreed,
                onCheckedChange = { onToggle() },
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = FestabookColor.black,
                        uncheckedColor = FestabookColor.gray400,
                        checkmarkColor = FestabookColor.white,
                    ),
            )
            Text(
                modifier = Modifier.clickable { uriHandler.openUri(PRIVACY_AGREEMENT_URL) },
                text = stringResource(Res.string.waiting_register_agreement_service),
                style = FestabookTypography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                color = FestabookColor.gray600,
            )
        }
    }
}

@Composable
private fun WaitingRegisterSubmitButton(
    isEnabled: Boolean,
    isSubmitting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .padding(
                    horizontal = festabookSpacing.paddingScreenGutter,
                    vertical = festabookSpacing.paddingBody4,
                ).fillMaxWidth()
                .height(52.dp)
                .clip(festabookShapes.radius2)
                .background(if (isEnabled) FestabookColor.black else FestabookColor.gray300)
                .clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = FestabookColor.white,
                strokeWidth = 4.dp,
            )
        } else {
            Text(
                text = stringResource(Res.string.waiting_register_submit),
                style = FestabookTypography.displayMedium,
                color = FestabookColor.white,
            )
        }
    }
}
