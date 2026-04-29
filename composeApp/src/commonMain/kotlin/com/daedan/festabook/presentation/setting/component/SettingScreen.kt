package com.daedan.festabook.presentation.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daedan.festabook.BuildKonfig
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.PermissionState
import com.daedan.festabook.presentation.common.ObserveAsEvents
import com.daedan.festabook.presentation.common.component.FestabookSwitch
import com.daedan.festabook.presentation.common.component.FestabookTopAppBar
import com.daedan.festabook.presentation.home.FestivalUiState
import com.daedan.festabook.presentation.home.HomeViewModel
import com.daedan.festabook.presentation.home.model.FestivalUiModel
import com.daedan.festabook.presentation.home.model.OrganizationUiModel
import com.daedan.festabook.presentation.setting.SettingViewModel
import com.daedan.festabook.presentation.setting.waitinginfo.WaitingInfoViewModel
import com.daedan.festabook.presentation.setting.waitinginfo.model.WaitingInfoUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_arrow_forward_right
import festabookkmp.composeapp.generated.resources.move
import festabookkmp.composeapp.generated.resources.setting_app_info_title
import festabookkmp.composeapp.generated.resources.setting_app_version
import festabookkmp.composeapp.generated.resources.setting_contact_us
import festabookkmp.composeapp.generated.resources.setting_current_university_notice
import festabookkmp.composeapp.generated.resources.setting_notice_enabled
import festabookkmp.composeapp.generated.resources.setting_notice_title
import festabookkmp.composeapp.generated.resources.setting_personal_information_policy
import festabookkmp.composeapp.generated.resources.setting_service_policy
import festabookkmp.composeapp.generated.resources.setting_title
import festabookkmp.composeapp.generated.resources.setting_waiting_info_not_registered
import festabookkmp.composeapp.generated.resources.setting_waiting_info_phone_number
import festabookkmp.composeapp.generated.resources.setting_waiting_info_section_title
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SERVICE_POLICY_URL: String =
    "https://www.notion.so/festabook-2026-04-01-335a540dc0b78055a450d7c82a1bdd40?source=copy_link"

private const val PERSONAL_INFORMATION_POLICY_URL: String =
    "https://www.notion.so/festabook-2026-04-01-335a540dc0b780fdb79cc83372595721?source=copy_link"

private const val CONTACT_US_URL =
    "https://forms.gle/XjqJFfQrTPgkZzGZ9"

@Composable
fun SettingRoute(
    notificationPermissionManager: NotificationPermissionManager,
    onShowSnackBar: (String) -> Unit,
    onShowErrorSnackBar: (Throwable) -> Unit,
    settingViewModel: SettingViewModel,
    homeViewModel: HomeViewModel,
    waitingInfoViewModel: WaitingInfoViewModel,
    onPhoneNumberClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val festival by homeViewModel.festivalUiState.collectAsStateWithLifecycle()
    val isUniversitySubscribed by settingViewModel.isAllowed.collectAsStateWithLifecycle()
    val isSubscribedLoading by settingViewModel.isLoading.collectAsStateWithLifecycle()
    val waitingInfoUiState by waitingInfoViewModel.waitingInfoUiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    val enableMessage = stringResource(Res.string.setting_notice_enabled)

    var showPermissionDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(flow = settingViewModel.permissionCheckEvent) {
        val permission = notificationPermissionManager.checkPermission()

        when (permission) {
            PermissionState.GRANTED -> {
                settingViewModel.saveNotificationId()
                onShowSnackBar(enableMessage)
            }

            PermissionState.NEED_RATIONALE -> {
                showPermissionDialog = true
            }

            PermissionState.DENIED -> {
                notificationPermissionManager.requestPermission()
            }
        }
    }

    ObserveAsEvents(flow = settingViewModel.error) {
        onShowErrorSnackBar(it)
    }

    LaunchedEffect(Unit) {
        waitingInfoViewModel.loadWaitingInfo()
    }

    if (showPermissionDialog) {
        NotificationPermissionDialog(
            onConfirm = {
                showPermissionDialog = false
                notificationPermissionManager.requestPermission()
            },
        )
    }

    val phoneNumber = (waitingInfoUiState as? WaitingInfoUiState.Registered)?.phoneNumber

    SettingScreen(
        modifier = modifier,
        festivalUiState = festival,
        isUniversitySubscribed = isUniversitySubscribed,
        appVersion = "v ${BuildKonfig.APP_VERSION_NAME}",
        isSubscribeEnabled = !isSubscribedLoading,
        phoneNumber = phoneNumber,
        onSubscribeClick = { settingViewModel.notificationAllowClick() },
        onServicePolicyClick = { uriHandler.openUri(SERVICE_POLICY_URL) },
        onPersonalInformationPolicyClick = { uriHandler.openUri(PERSONAL_INFORMATION_POLICY_URL) },
        onContactUsClick = { uriHandler.openUri(CONTACT_US_URL) },
        onPhoneNumberClick = onPhoneNumberClick,
        onError = {
            onShowErrorSnackBar(it.throwable)
        },
    )
}

@Composable
fun SettingScreen(
    festivalUiState: FestivalUiState,
    isUniversitySubscribed: Boolean,
    appVersion: String,
    isSubscribeEnabled: Boolean,
    phoneNumber: String?,
    modifier: Modifier = Modifier,
    onSubscribeClick: (Boolean) -> Unit = {},
    onServicePolicyClick: () -> Unit = {},
    onPersonalInformationPolicyClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    onPhoneNumberClick: () -> Unit = {},
    onError: (FestivalUiState.Error) -> Unit = {},
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidthDp =
        remember {
            with(density) {
                windowInfo.containerSize.width.toDp()
            }
        }

    val currentOnError by rememberUpdatedState(onError)

    LaunchedEffect(festivalUiState) {
        when (festivalUiState) {
            is FestivalUiState.Error -> currentOnError(festivalUiState)
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            FestabookTopAppBar(
                title = stringResource(Res.string.setting_title),
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = FestabookColor.white)
                    .padding(horizontal = festabookSpacing.paddingScreenGutter)
                    .padding(innerPadding),
        ) {
            when (festivalUiState) {
                is FestivalUiState.Success -> {
                    SubscriptionContent(
                        universityName = festivalUiState.organization.organizationName,
                        isUniversitySubscribed = isUniversitySubscribed,
                        onSubscribeClick = onSubscribeClick,
                        isSubscribeEnabled = isSubscribeEnabled,
                    )
                }

                else -> {}
            }

            HorizontalDivider(
                modifier =
                    Modifier
                        .requiredWidth(screenWidthDp)
                        .padding(vertical = 28.dp),
                color = FestabookColor.gray200,
                thickness = festabookSpacing.paddingBody1,
            )

            RegistrationInfoContent(
                phoneNumber = phoneNumber,
                onPhoneNumberClick = onPhoneNumberClick,
                screenWidthDp = screenWidthDp,
            )

            HorizontalDivider(
                modifier =
                    Modifier
                        .requiredWidth(screenWidthDp)
                        .padding(
                            top = 16.dp,
                            bottom = 28.dp,
                        ),
                color = FestabookColor.gray200,
                thickness = festabookSpacing.paddingBody1,
            )

            AppInfoContent(
                appVersion = appVersion,
                onServicePolicyClick = onServicePolicyClick,
                onPersonalInformationPolicyClick = onPersonalInformationPolicyClick,
                onContactUsClick = onContactUsClick,
            )
        }
    }
}

@Composable
private fun RegistrationInfoContent(
    phoneNumber: String?,
    onPhoneNumberClick: () -> Unit,
    screenWidthDp: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.setting_waiting_info_section_title),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray500,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .requiredWidth(screenWidthDp)
                    .clickable { onPhoneNumberClick() }
                    .padding(horizontal = festabookSpacing.paddingScreenGutter)
                    .padding(
                        vertical = 14.dp,
                    ),
        ) {
            Text(
                text = stringResource(Res.string.setting_waiting_info_phone_number),
                style = FestabookTypography.titleMedium,
            )

            Text(
                text =
                    phoneNumber
                        ?: stringResource(Res.string.setting_waiting_info_not_registered),
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.gray500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SubscriptionContent(
    universityName: String,
    isUniversitySubscribed: Boolean,
    isSubscribeEnabled: Boolean,
    onSubscribeClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.setting_notice_title),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray500,
            modifier = Modifier.padding(top = 20.dp),
        )

        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.setting_current_university_notice),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = FestabookTypography.titleMedium,
                    modifier =
                        Modifier.padding(
                            top = festabookSpacing.paddingBody3,
                        ),
                )

                Text(
                    text = universityName,
                    style = FestabookTypography.bodyMedium,
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = FestabookColor.gray500,
                )
            }

            FestabookSwitch(
                enabled = isSubscribeEnabled,
                checked = isUniversitySubscribed,
                onCheckedChange = onSubscribeClick,
            )
        }
    }
}

@Composable
private fun AppInfoContent(
    appVersion: String,
    onServicePolicyClick: () -> Unit,
    onPersonalInformationPolicyClick: () -> Unit,
    onContactUsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.setting_app_info_title),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray500,
        )

        AppVersionInfo(
            appVersion = appVersion,
        )

        AppInfoButton(
            text = stringResource(Res.string.setting_service_policy),
            onClick = onServicePolicyClick,
        )

        AppInfoButton(
            text = stringResource(Res.string.setting_personal_information_policy),
            onClick = onPersonalInformationPolicyClick,
        )

        AppInfoButton(
            text = stringResource(Res.string.setting_contact_us),
            onClick = onContactUsClick,
        )
    }
}

@Composable
private fun AppVersionInfo(
    appVersion: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
    ) {
        Text(
            text = stringResource(Res.string.setting_app_version),
            style = FestabookTypography.titleMedium,
        )

        Text(
            text = appVersion,
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray500,
        )
    }
}

@Composable
private fun AppInfoButton(
    text: String,
    onClick: () -> Unit,
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

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .requiredWidth(screenWidthDp)
                .clickable {
                    onClick()
                }.padding(
                    horizontal = festabookSpacing.paddingScreenGutter,
                    vertical = festabookSpacing.paddingBody3,
                ),
    ) {
        Text(
            text = text,
            style = FestabookTypography.titleMedium,
        )

        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward_right),
            contentDescription = stringResource(Res.string.move),
            tint = FestabookColor.gray300,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    FestabookTheme {
        var isSubscribed by remember { mutableStateOf(false) }
        SettingScreen(
            festivalUiState =
                FestivalUiState.Success(
                    OrganizationUiModel(
                        id = 1,
                        organizationName = "성균관대학교 인문사회과학철학문학자연캠퍼스 인문사회과학철학문학자연캠퍼스",
                        festival =
                            FestivalUiModel(
                                festivalName = "성균관대학교 축제축제축제축제축제축제축제축제축제축제축제축제",
                                festivalImages = listOf(),
                                startDate = LocalDate(2026, 1, 1),
                                endDate = LocalDate(2026, 1, 2),
                                sponsors = emptyList(),
                                instagramLink = null,
                                homepageLink = null,
                            ),
                    ),
                ),
            isUniversitySubscribed = isSubscribed,
            onSubscribeClick = { isSubscribed = !isSubscribed },
            appVersion = "v1.0.0",
            isSubscribeEnabled = true,
            phoneNumber = null,
        )
    }
}
