package com.daedan.festabook.presentation.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.setting.waitinginfo.WaitingInfoUiState
import com.daedan.festabook.presentation.setting.waitinginfo.WaitingInfoViewModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_arrow_back
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_button
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_hint
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_label
import festabookkmp.composeapp.generated.resources.setting_waiting_info_add_title
import festabookkmp.composeapp.generated.resources.setting_waiting_info_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddWaitingInfoRoute(
    viewModel: WaitingInfoViewModel,
    onBackClick: () -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val waitingInfoUiState by viewModel.waitingInfoUiState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.saveSuccessEvent) {
        onBackClick()
    }
    ObserveAsEvents(flow = viewModel.errorEvent) {
        onShowErrorSnackBar(it)
    }

    when (val state = waitingInfoUiState) {
        is WaitingInfoUiState.Error -> {
            ErrorStateScreen(
                modifier = modifier,
                onRetry = viewModel::loadWaitingInfo,
            )
        }

        else -> {
            val initialPhoneNumber = (state as? WaitingInfoUiState.Registered)?.phoneNumber ?: ""
            AddWaitingInfoScreen(
                modifier = modifier,
                initialPhoneNumber = initialPhoneNumber,
                isLoading = isSaving,
                onSaveClick = viewModel::saveWaitingInfo,
                onBackClick = onBackClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaitingInfoScreen(
    initialPhoneNumber: String,
    isLoading: Boolean,
    onSaveClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var phoneNumber by remember(initialPhoneNumber) { mutableStateOf(initialPhoneNumber) }
    val isSaveEnabled = phoneNumber.count { it.isDigit() } >= 9 && !isLoading

    Scaffold(
        topBar = {
            TopAppBar(
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
                    .padding(horizontal = festabookSpacing.paddingScreenGutter)
                    .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(festabookSpacing.paddingBody5))

            Text(
                text = stringResource(Res.string.setting_waiting_info_add_label),
                style = FestabookTypography.bodyMedium,
                color = FestabookColor.gray600,
            )

            Spacer(modifier = Modifier.height(festabookSpacing.paddingBody2))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    phoneNumber = input.filter { it.isDigit() || it == '-' }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.setting_waiting_info_add_hint),
                        style = FestabookTypography.bodyMedium,
                        color = FestabookColor.gray400,
                    )
                },
                textStyle = FestabookTypography.bodyMedium,
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { if (isSaveEnabled) onSaveClick(phoneNumber) },
                    ),
                shape = RoundedCornerShape(12.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FestabookColor.black,
                        unfocusedBorderColor = FestabookColor.gray200,
                        focusedTextColor = FestabookColor.black,
                        unfocusedTextColor = FestabookColor.black,
                        cursorColor = FestabookColor.black,
                    ),
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSaveClick(phoneNumber) },
                enabled = isSaveEnabled,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = festabookSpacing.paddingBody5),
                shape = RoundedCornerShape(12.dp),
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
                    style = FestabookTypography.titleMedium,
                    modifier = Modifier.padding(vertical = festabookSpacing.paddingBody2),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddWaitingInfoScreenEmptyPreview() {
    AddWaitingInfoScreen(
        initialPhoneNumber = "",
        isLoading = false,
        onSaveClick = {},
        onBackClick = {},
    )
}

@Preview
@Composable
private fun AddWaitingInfoScreenFilledPreview() {
    AddWaitingInfoScreen(
        initialPhoneNumber = "010-1234-5678",
        isLoading = false,
        onSaveClick = {},
        onBackClick = {},
    )
}