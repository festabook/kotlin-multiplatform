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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.CoilImage
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.placeMap.component.PlaceCategoryLabel
import com.daedan.festabook.presentation.placeMap.waitingRegister.WaitingRegisterViewModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingPlaceSummaryUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.model.WaitingRegisterUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.btn_back_to_previous
import festabookkmp.composeapp.generated.resources.content_description_booth_image
import festabookkmp.composeapp.generated.resources.content_description_iv_clock
import festabookkmp.composeapp.generated.resources.content_description_iv_host
import festabookkmp.composeapp.generated.resources.content_description_iv_location
import festabookkmp.composeapp.generated.resources.content_description_waiting_party_size_decrease
import festabookkmp.composeapp.generated.resources.content_description_waiting_party_size_increase
import festabookkmp.composeapp.generated.resources.content_description_waiting_register_back
import festabookkmp.composeapp.generated.resources.ic_location
import festabookkmp.composeapp.generated.resources.ic_place_detail_clock
import festabookkmp.composeapp.generated.resources.ic_place_detail_host
import festabookkmp.composeapp.generated.resources.place_list_default_title
import festabookkmp.composeapp.generated.resources.waiting_register_agreement_marketing
import festabookkmp.composeapp.generated.resources.waiting_register_agreement_service
import festabookkmp.composeapp.generated.resources.waiting_register_agreement_title
import festabookkmp.composeapp.generated.resources.waiting_register_party_size_limit_notice
import festabookkmp.composeapp.generated.resources.waiting_register_party_size_title
import festabookkmp.composeapp.generated.resources.waiting_register_submit
import festabookkmp.composeapp.generated.resources.waiting_register_success
import festabookkmp.composeapp.generated.resources.waiting_register_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WaitingRegisterRoute(
    viewModel: WaitingRegisterViewModel,
    onBackToPreviousClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val successMessage = stringResource(Res.string.waiting_register_success)

    ObserveAsEvents(viewModel.registerSuccessEvent) {
        onShowSnackbar(successMessage)
        onBackToPreviousClick()
    }
    ObserveAsEvents(viewModel.registerFailureEvent) { throwable ->
        onShowErrorSnackbar(throwable)
    }

    WaitingRegisterScreen(
        uiState = uiState,
        onBackToPreviousClick = onBackToPreviousClick,
        onIncreasePartySize = viewModel::increasePartySize,
        onDecreasePartySize = viewModel::decreasePartySize,
        onToggleServiceAgreement = viewModel::toggleServiceAgreement,
        onToggleMarketingAgreement = viewModel::toggleMarketingAgreement,
        onSubmit = viewModel::submitWaitingRegister,
        onShowErrorSnackbar = onShowErrorSnackbar,
        modifier = modifier,
    )
}

@Composable
fun WaitingRegisterScreen(
    uiState: WaitingRegisterUiState,
    onBackToPreviousClick: () -> Unit,
    onIncreasePartySize: () -> Unit,
    onDecreasePartySize: () -> Unit,
    onToggleServiceAgreement: () -> Unit,
    onToggleMarketingAgreement: () -> Unit,
    onSubmit: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    val state = rememberNavigationEventState(NavigationEventInfo.None)
    val isSubmitting = (uiState as? WaitingRegisterUiState.Success)?.isSubmitting ?: false

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
        WaitingRegisterTopBar(onBackClick = onBackToPreviousClick)

        when (uiState) {
            is WaitingRegisterUiState.Loading -> {
                Box(modifier = Modifier.weight(1f)) {
                    LoadingStateScreen()
                }
            }

            is WaitingRegisterUiState.Error -> {
                Box(modifier = Modifier.weight(1f)) {
                    ErrorStateScreen()
                }
            }

            is WaitingRegisterUiState.Success -> {
                Box(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        WaitingPlaceSummaryCard(
                            summary = uiState.placeSummary,
                            modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
                        )

                        SectionDivider()

                        PartySizeSection(
                            partySize = uiState.partySize,
                            canDecrease = uiState.canDecreasePartySize,
                            canIncrease = uiState.canIncreasePartySize,
                            onDecrease = onDecreasePartySize,
                            onIncrease = onIncreasePartySize,
                        )

                        SectionDivider()

                        AgreementSection(
                            isServiceAgreed = uiState.isServiceAgreed,
                            isMarketingAgreed = uiState.isMarketingAgreed,
                            onToggleService = onToggleServiceAgreement,
                            onToggleMarketing = onToggleMarketingAgreement,
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    WaitingRegisterSubmitButton(
                        isEnabled = uiState.canSubmit,
                        isSubmitting = uiState.isSubmitting,
                        onClick = onSubmit,
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
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Image(
            modifier = Modifier
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
            style = FestabookTypography.displayMedium,
        )
    }
}

@Composable
private fun WaitingPlaceSummaryCard(
    summary: WaitingPlaceSummaryUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = festabookSpacing.paddingScreenGutter)
            .cardBackground(
                backgroundColor = FestabookColor.white,
                shape = festabookShapes.radius4,
            )
            .padding(festabookSpacing.paddingBody4),
    ) {
        PlaceCategoryLabel(category = summary.category)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = festabookSpacing.paddingBody2),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = summary.title ?: stringResource(Res.string.place_list_default_title),
                    style = FestabookTypography.displaySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                if (summary.startTime != null) {
                    PlaceInfoItem(
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
                        painter = painterResource(Res.drawable.ic_place_detail_clock),
                        contentDescription = stringResource(Res.string.content_description_iv_clock),
                        text = buildString {
                            append(summary.startTime)
                            summary.endTime?.let { append(" ~ $it") }
                        },
                    )
                }

                if (summary.location != null) {
                    PlaceInfoItem(
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
                        painter = painterResource(Res.drawable.ic_location),
                        contentDescription = stringResource(Res.string.content_description_iv_location),
                        text = summary.location,
                    )
                }

                if (summary.host != null) {
                    PlaceInfoItem(
                        modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
                        painter = painterResource(Res.drawable.ic_place_detail_host),
                        contentDescription = stringResource(Res.string.content_description_iv_host),
                        text = summary.host,
                    )
                }
            }

            if (summary.imageUrl != null) {
                CoilImage(
                    url = summary.imageUrl,
                    contentDescription = stringResource(Res.string.content_description_booth_image),
                    modifier = Modifier
                        .padding(start = festabookSpacing.paddingBody4)
                        .size(88.dp)
                        .clip(festabookShapes.radius2),
                )
            }
        }

        if (summary.description != null) {
            Text(
                modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
                text = summary.description,
                style = FestabookTypography.bodySmall,
                color = FestabookColor.gray500,
            )
        }
    }
}

@Composable
private fun PlaceInfoItem(
    painter: Painter,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = FestabookColor.gray500,
        )
        Text(
            modifier = Modifier.padding(start = festabookSpacing.paddingBody1),
            text = text,
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray500,
        )
    }
}

@Composable
private fun SectionDivider(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(FestabookColor.white),
    )
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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = festabookSpacing.paddingBody4),
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
        modifier = modifier
            .size(44.dp)
            .border(
                width = 1.dp,
                color = if (enabled) FestabookColor.gray400 else FestabookColor.gray200,
                shape = CircleShape,
            )
            .clip(CircleShape)
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
    isServiceAgreed: Boolean,
    isMarketingAgreed: Boolean,
    onToggleService: () -> Unit,
    onToggleMarketing: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = festabookSpacing.paddingScreenGutter,
                vertical = festabookSpacing.paddingBody4,
            ),
    ) {
        Text(
            text = stringResource(Res.string.waiting_register_agreement_title),
            style = FestabookTypography.displayMedium,
        )

        AgreementCheckboxRow(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody4),
            label = stringResource(Res.string.waiting_register_agreement_service),
            checked = isServiceAgreed,
            onToggle = onToggleService,
        )

        AgreementCheckboxRow(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
            label = stringResource(Res.string.waiting_register_agreement_marketing),
            checked = isMarketingAgreed,
            onToggle = onToggleMarketing,
        )
    }
}

@Composable
private fun AgreementCheckboxRow(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = FestabookColor.accentBlue,
                uncheckedColor = FestabookColor.gray400,
            ),
        )
        Text(
            modifier = Modifier.padding(start = festabookSpacing.paddingBody1),
            text = label,
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray500,
        )
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
        modifier = modifier
            .padding(
                horizontal = festabookSpacing.paddingScreenGutter,
                vertical = festabookSpacing.paddingBody4,
            )
            .fillMaxWidth()
            .height(52.dp)
            .clip(festabookShapes.radius2)
            .background(if (isEnabled) FestabookColor.accentBlue else FestabookColor.gray300)
            .clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = FestabookColor.white,
                strokeWidth = 2.dp,
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
