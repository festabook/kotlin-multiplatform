package com.daedan.festabook.presentation.setting.waitinginfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.setting.waitinginfo.WaitingInfoViewModel
import com.daedan.festabook.presentation.setting.waitinginfo.model.WaitingInfoUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_arrow_back
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_button
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_description
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_headline
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_hint
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_label
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_phone_icon
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_terms_item
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_terms_title
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_title
import festabookkmp.composeapp.generated.resources.setting_waiting_info_back
import festabookkmp.composeapp.generated.resources.setting_waiting_info_save_success
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val TERMS_URL =
    "https://www.notion.so/2026-04-01-335a540dc0b780fa9897e0a3bdf45bae"

@Composable
fun AddWaitingInfoRoute(
    viewModel: WaitingInfoViewModel,
    onBackClick: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val waitingInfoUiState by viewModel.waitingInfoUiState.collectAsStateWithLifecycle()
    val phoneNumber by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val isTermsAgreed by viewModel.isTermsAgreed.collectAsStateWithLifecycle()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsStateWithLifecycle()
    val saveSuccessMessage = stringResource(Res.string.setting_waiting_info_save_success)

    ObserveAsEvents(flow = viewModel.saveSuccessEvent) {
        onShowSnackBar(saveSuccessMessage)
        onBackClick()
    }
    ObserveAsEvents(flow = viewModel.errorEvent) {
        onShowErrorSnackBar(it)
    }

    LaunchedEffect(Unit) {
        viewModel.loadWaitingInfo()
    }

    when (waitingInfoUiState) {
        is WaitingInfoUiState.Error -> {
            ErrorStateScreen(
                modifier = modifier,
            )
        }

        else -> {
            AddWaitingInfoScreen(
                modifier = modifier,
                phoneNumber = phoneNumber,
                isTermsAgreed = isTermsAgreed,
                isSaveEnabled = isSaveEnabled,
                onPhoneNumberChange = viewModel::updatePhoneNumber,
                onTermsAgreedChange = viewModel::setTermsAgreed,
                onSaveClick = viewModel::saveWaitingInfo,
                onBackClick = onBackClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaitingInfoScreen(
    phoneNumber: String,
    isTermsAgreed: Boolean,
    isSaveEnabled: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    onTermsAgreedChange: (Boolean) -> Unit,
    onSaveClick: (String) -> Unit,
    onBackClick: () -> Unit,
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.setting_waiting_info_add_title),
                        style = FestabookTypography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = stringResource(Res.string.setting_waiting_info_back),
                            tint = FestabookColor.black,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = FestabookColor.white,
                    ),
            )
        },
        modifier = modifier,
        containerColor = FestabookColor.white,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(FestabookColor.white)
                    .padding(innerPadding)
                    .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(festabookSpacing.paddingBody5))
            Text(
                text = stringResource(Res.string.setting_waiting_info_add_headline),
                style = FestabookTypography.displayMedium,
                color = FestabookColor.black,
                modifier = Modifier.padding(horizontal = festabookSpacing.paddingScreenGutter),
            )
            Spacer(modifier = Modifier.height(40.dp))

            HorizontalDivider(
                color = FestabookColor.gray200,
                thickness = festabookSpacing.paddingBody1,
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = festabookSpacing.paddingScreenGutter),
            ) {
                AddPhoneNumberContent(
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = onPhoneNumberChange,
                    modifier = Modifier.padding(vertical = 28.dp),
                )

                HorizontalDivider(
                    color = FestabookColor.gray200,
                    thickness = festabookSpacing.paddingBody1,
                    modifier = Modifier.requiredWidth(screenWidthDp),
                )

                Spacer(modifier = Modifier.height(28.dp))

                WaitingInfoAddTerms(
                    isTermsAgreed = isTermsAgreed,
                    onTermsAgreedChange = onTermsAgreedChange,
                )

                Spacer(modifier = Modifier.weight(1f))

                ConfirmButton(
                    phoneNumber = phoneNumber,
                    isSaveEnabled = isSaveEnabled,
                    onSaveClick = onSaveClick,
                )
            }
        }
    }
}

@Composable
private fun AddPhoneNumberContent(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.setting_waiting_info_add_label),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody1))
        Text(
            text = stringResource(Res.string.setting_waiting_info_add_description),
            style = FestabookTypography.bodySmall,
            color = FestabookColor.gray400,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody3))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(Res.string.setting_waiting_info_add_hint),
                    style = FestabookTypography.titleMedium,
                    color = FestabookColor.gray400,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = stringResource(Res.string.setting_waiting_info_add_phone_icon),
                    tint = FestabookColor.gray400,
                )
            },
            textStyle = FestabookTypography.titleMedium,
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                ),
            shape = festabookShapes.radius2,
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FestabookColor.gray100,
                    unfocusedContainerColor = FestabookColor.gray100,
                    focusedBorderColor = FestabookColor.black,
                    unfocusedBorderColor = FestabookColor.gray200,
                    focusedTextColor = FestabookColor.black,
                    unfocusedTextColor = FestabookColor.black,
                    cursorColor = FestabookColor.black,
                ),
        )
    }
}

@Composable
private fun WaitingInfoAddTerms(
    isTermsAgreed: Boolean,
    onTermsAgreedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.setting_waiting_info_add_terms_title),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody1))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.clip(festabookShapes.radius1),
                checked = isTermsAgreed,
                onCheckedChange = onTermsAgreedChange,
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = FestabookColor.black,
                        uncheckedColor = FestabookColor.gray400,
                        checkmarkColor = FestabookColor.white,
                    ),
            )
            val uriHandler = LocalUriHandler.current
            Text(
                text = stringResource(Res.string.setting_waiting_info_add_terms_item),
                style = FestabookTypography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                color = FestabookColor.gray600,
                modifier =
                    Modifier.clickable {
                        uriHandler.openUri(TERMS_URL)
                    },
            )
        }
    }
}

@Composable
private fun ConfirmButton(
    phoneNumber: String,
    isSaveEnabled: Boolean,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onSaveClick(phoneNumber) },
        enabled = isSaveEnabled,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(bottom = festabookSpacing.paddingBody5),
        shape = festabookShapes.radius3,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = FestabookColor.black,
                contentColor = FestabookColor.white,
                disabledContainerColor = FestabookColor.gray200,
                disabledContentColor = FestabookColor.gray400,
            ),
    ) {
        Text(
            text = stringResource(Res.string.setting_waiting_info_add_button),
            style = FestabookTypography.displaySmall,
            modifier = Modifier.padding(vertical = festabookSpacing.paddingBody2),
        )
    }
}

@Preview
@Composable
private fun AddWaitingInfoScreenEmptyPreview() {
    AddWaitingInfoScreen(
        phoneNumber = "",
        isTermsAgreed = false,
        isSaveEnabled = false,
        onPhoneNumberChange = {},
        onTermsAgreedChange = {},
        onSaveClick = {},
        onBackClick = {},
    )
}

@Preview
@Composable
private fun AddWaitingInfoScreenFilledPreview() {
    AddWaitingInfoScreen(
        phoneNumber = "010-1234-5678",
        isTermsAgreed = true,
        isSaveEnabled = true,
        onPhoneNumberChange = {},
        onTermsAgreedChange = {},
        onSaveClick = {},
        onBackClick = {},
    )
}
