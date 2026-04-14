package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.FestabookImage
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.common.component.URLText
import com.daedan.festabook.presentation.placeMap.component.PlaceCategoryLabel
import com.daedan.festabook.presentation.placeMap.model.PlaceCategoryUiModel
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.PlaceDetailViewModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.ImageUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiState
import com.daedan.festabook.presentation.placeMap.placeDetail.model.WaitingUiState
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.btn_back_to_previous
import festabookkmp.composeapp.generated.resources.content_description_exit_place_detail
import festabookkmp.composeapp.generated.resources.content_description_iv_clock
import festabookkmp.composeapp.generated.resources.content_description_iv_host
import festabookkmp.composeapp.generated.resources.content_description_iv_location
import festabookkmp.composeapp.generated.resources.format_date
import festabookkmp.composeapp.generated.resources.ic_location
import festabookkmp.composeapp.generated.resources.ic_place_detail_clock
import festabookkmp.composeapp.generated.resources.ic_place_detail_host
import festabookkmp.composeapp.generated.resources.place_detail_default_host
import festabookkmp.composeapp.generated.resources.place_detail_default_time
import festabookkmp.composeapp.generated.resources.place_detail_real_time_waiting
import festabookkmp.composeapp.generated.resources.place_detail_waiting_closed_btn
import festabookkmp.composeapp.generated.resources.place_detail_waiting_current_teams
import festabookkmp.composeapp.generated.resources.place_detail_waiting_estimated_time
import festabookkmp.composeapp.generated.resources.place_detail_waiting_inactive
import festabookkmp.composeapp.generated.resources.place_detail_waiting_register
import festabookkmp.composeapp.generated.resources.place_detail_waiting_teams_count
import festabookkmp.composeapp.generated.resources.place_list_default_description
import festabookkmp.composeapp.generated.resources.place_list_default_location
import festabookkmp.composeapp.generated.resources.place_list_default_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlaceDetailRoute(
    viewModel: PlaceDetailViewModel,
    onBackToPreviousClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeDetailUiState by viewModel.placeDetail.collectAsStateWithLifecycle()
    PlaceDetailScreen(
        modifier = modifier,
        uiState = placeDetailUiState,
        onBackToPreviousClick = onBackToPreviousClick,
        onShowErrorSnackbar = onShowErrorSnackbar,
    )
}

@Composable
fun PlaceDetailScreen(
    uiState: PlaceDetailUiState,
    onBackToPreviousClick: () -> Unit,
    modifier: Modifier = Modifier,
    onShowErrorSnackbar: (Throwable) -> Unit = {}, // TODO Fragment 제거 시 필수 파라미터로 변경
) {
    val scrollState = rememberScrollState()
    val currentOnShowErrorSnackbar by rememberUpdatedState(onShowErrorSnackbar)
    var isDialogOpen by remember { mutableStateOf(false) }
    val state = rememberNavigationEventState(NavigationEventInfo.None)

    NavigationBackHandler(
        state = state,
        isBackEnabled = !isDialogOpen,
    ) {
        onBackToPreviousClick()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is PlaceDetailUiState.Error -> {
                currentOnShowErrorSnackbar(uiState.throwable)
            }

            else -> {
                Unit
            }
        }
    }

    when (uiState) {
        is PlaceDetailUiState.Success -> {
            val pagerState =
                rememberPagerState(
                    pageCount = { uiState.placeDetail.images.size },
                )

            PlaceDetailImageDialog(
                isDialogOpen = isDialogOpen,
                onDismissRequest = { isDialogOpen = false },
                pagerState = pagerState,
                images = uiState.placeDetail.images,
            )

            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(color = FestabookColor.white)
                            .verticalScroll(scrollState),
                ) {
                    PlaceDetailImageContent(
                        images = uiState.placeDetail.images,
                        onBackToPreviousClick = onBackToPreviousClick,
                        onPageUpdate = { pagerState.scrollToPage(it) },
                        modifier =
                            Modifier
                                .clickable { isDialogOpen = true }
                                .fillMaxWidth(),
                    )

                    PlaceDetailContent(placeDetail = uiState.placeDetail)

                    PlaceWaitingSection(waiting = uiState.waiting)

                    PlaceDetailDescription(placeDetail = uiState.placeDetail)

                    if (uiState.waiting is WaitingUiState.Active || uiState.waiting is WaitingUiState.Closed) {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }

                PlaceDetailBottomBar(
                    waiting = uiState.waiting,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }

        is PlaceDetailUiState.Loading -> {
            LoadingStateScreen()
        }

        is PlaceDetailUiState.Error -> {
            EmptyStateScreen()
        }
    }
}

@Composable
private fun PlaceDetailImageDialog(
    isDialogOpen: Boolean,
    pagerState: PagerState,
    images: List<ImageUiModel>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isDialogOpen) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties =
                DialogProperties(
                    usePlatformDefaultWidth = false,
                ),
        ) {
            Box(
                modifier =
                    modifier
                        .fillMaxSize()
                        .background(FestabookColor.black.copy(alpha = 0.8f)),
            ) {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.CenterVertically,
                    beyondViewportPageCount = 5,
                ) { page ->

                    FestabookImage(
                        imageUrl = images[page].url,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        isZoomable = true,
                        enablePopUp = false,
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "close the popup",
                        tint = FestabookColor.white,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceDetailImageContent(
    images: List<ImageUiModel>,
    onBackToPreviousClick: (() -> Unit),
    modifier: Modifier = Modifier,
    onPageUpdate: suspend (page: Int) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { images.size })
    val currentOnPageUpdate by rememberUpdatedState(onPageUpdate)
    LaunchedEffect(pagerState.settledPage) {
        currentOnPageUpdate(pagerState.settledPage)
    }

    Box(modifier = modifier) {
        BackToPreviousButton(
            modifier =
                Modifier
                    .padding(
                        top = festabookSpacing.paddingBody4,
                        start = festabookSpacing.paddingScreenGutter,
                    ).zIndex(1f),
            onClick = onBackToPreviousClick,
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            beyondViewportPageCount = 5,
        ) { page ->
            FestabookImage(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                imageUrl = images[page].url,
            )
        }

        PagerIndicator(
            pagerState = pagerState,
            modifier =
                Modifier
                    .height(24.dp)
                    .align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun PlaceDetailContent(
    placeDetail: PlaceDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = festabookSpacing.paddingScreenGutter),
    ) {
        PlaceCategoryLabel(
            modifier = Modifier.padding(top = 24.dp),
            category = placeDetail.place.category,
        )

        Text(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
            text = placeDetail.place.title ?: stringResource(Res.string.place_list_default_title),
            style = FestabookTypography.displayMedium,
        )

        PlaceDetailInfo(placeDetail = placeDetail)
    }
}

@Composable
private fun PlaceDetailDescription(
    placeDetail: PlaceDetailUiModel,
    modifier: Modifier = Modifier,
) {
    var isDescriptionExpand by remember { mutableStateOf(true) }

    URLText(
        modifier =
            modifier
                .animateContentSize(
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium,
                        ),
                ).padding(
                    horizontal = festabookSpacing.paddingScreenGutter,
                    vertical = festabookSpacing.paddingBody3,
                ),
        onClick = {
            isDescriptionExpand = !isDescriptionExpand
        },
        text =
            placeDetail.place.description
                ?: stringResource(Res.string.place_list_default_description),
        style = FestabookTypography.bodySmall,
        maxLines =
            if (isDescriptionExpand) {
                Int.MAX_VALUE
            } else {
                1
            },
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun PlaceWaitingSection(
    waiting: WaitingUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    horizontal = festabookSpacing.paddingScreenGutter,
                    vertical = festabookSpacing.paddingBody4,
                ),
    ) {
        HorizontalDivider(color = FestabookColor.gray200)

        Text(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody4),
            text = stringResource(Res.string.place_detail_real_time_waiting),
            style = FestabookTypography.titleMedium,
        )

        when (waiting) {
            is WaitingUiState.Active ->
                WaitingTeamsRow(
                    totalTeams = waiting.totalTeams,
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody3),
                )

            is WaitingUiState.Closed ->
                WaitingTeamsRow(
                    totalTeams = waiting.totalTeams,
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody3),
                )

            is WaitingUiState.Inactive ->
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = festabookSpacing.paddingBody3)
                            .background(FestabookColor.gray100, RoundedCornerShape(8.dp))
                            .padding(
                                horizontal = festabookSpacing.paddingScreenGutter,
                                vertical = festabookSpacing.paddingBody4,
                            ),
                    text = stringResource(Res.string.place_detail_waiting_inactive),
                    style = FestabookTypography.bodyMedium,
                    color = FestabookColor.gray500,
                )

            is WaitingUiState.Loading -> Unit
        }
    }
}

@Composable
private fun WaitingTeamsRow(
    totalTeams: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(FestabookColor.black, RoundedCornerShape(50.dp))
                .padding(
                    horizontal = festabookSpacing.paddingScreenGutter,
                    vertical = 18.dp,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.place_detail_waiting_current_teams),
            style = FestabookTypography.bodyMedium,
            color = FestabookColor.white,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(Res.string.place_detail_waiting_teams_count, totalTeams),
            style = FestabookTypography.displaySmall,
            color = FestabookColor.white,
        )

        Spacer(modifier = Modifier.width(festabookSpacing.paddingBody2))

        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = FestabookColor.white,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun PlaceDetailBottomBar(
    waiting: WaitingUiState,
    modifier: Modifier = Modifier,
) {
    when (waiting) {
        is WaitingUiState.Active -> {
            Column(modifier = modifier.fillMaxWidth()) {
                HorizontalDivider(color = FestabookColor.gray200)
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(FestabookColor.white)
                            .padding(
                                horizontal = festabookSpacing.paddingScreenGutter,
                                vertical = festabookSpacing.paddingBody3,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text =
                            stringResource(
                                Res.string.place_detail_waiting_estimated_time,
                                waiting.estimatedMinutes,
                            ),
                        style = FestabookTypography.bodySmall,
                        color = FestabookColor.gray500,
                    )

                    Spacer(modifier = Modifier.width(festabookSpacing.paddingBody3))

                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .background(FestabookColor.accentBlue, RoundedCornerShape(8.dp))
                                .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.place_detail_waiting_register),
                            style = FestabookTypography.titleSmall,
                            color = FestabookColor.white,
                        )
                    }
                }
            }
        }

        is WaitingUiState.Closed -> {
            Column(modifier = modifier.fillMaxWidth()) {
                HorizontalDivider(color = FestabookColor.gray200)
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(FestabookColor.white)
                            .padding(
                                horizontal = festabookSpacing.paddingScreenGutter,
                                vertical = festabookSpacing.paddingBody3,
                            ),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(FestabookColor.gray200, RoundedCornerShape(8.dp))
                                .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.place_detail_waiting_closed_btn),
                            style = FestabookTypography.titleSmall,
                            color = FestabookColor.gray500,
                        )
                    }
                }
            }
        }

        else -> Unit
    }
}

@Composable
private fun PlaceDetailInfo(
    placeDetail: PlaceDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PlaceDetailInfoItem(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody4),
            painter = painterResource(Res.drawable.ic_place_detail_clock),
            contentDescription = stringResource(Res.string.content_description_iv_clock),
            text = formattedDate(placeDetail.startTime, placeDetail.endTime),
        )

        PlaceDetailInfoItem(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
            painter = painterResource(Res.drawable.ic_location),
            contentDescription = stringResource(Res.string.content_description_iv_location),
            text =
                placeDetail.place.location
                    ?: stringResource(Res.string.place_list_default_location),
        )

        PlaceDetailInfoItem(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
            painter = painterResource(Res.drawable.ic_place_detail_host),
            contentDescription = stringResource(Res.string.content_description_iv_host),
            text =
                placeDetail.host
                    ?: stringResource(Res.string.place_detail_default_host),
        )
    }
}

@Composable
private fun PlaceDetailInfoItem(
    painter: Painter,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
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
private fun BackToPreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier =
            modifier
                .size(30.dp)
                .clickable { onClick() },
        painter = painterResource(Res.drawable.btn_back_to_previous),
        contentDescription = stringResource(Res.string.content_description_exit_place_detail),
    )
}

@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val isSelected = pagerState.currentPage == iteration
            val color = if (isSelected) FestabookColor.black else FestabookColor.gray300
            val size by animateDpAsState(targetValue = if (isSelected) 10.dp else 8.dp)

            Box(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size),
            )
        }
    }
}

@Composable
private fun formattedDate(
    startTime: String?,
    endTime: String?,
): String =
    if (startTime == null && endTime == null) {
        stringResource(Res.string.place_detail_default_time)
    } else {
        stringResource(Res.string.format_date, startTime.toString(), endTime.toString())
    }

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenActivePreview() {
    FestabookTheme {
        PlaceDetailScreen(
            onBackToPreviousClick = {},
            onShowErrorSnackbar = {},
            uiState =
                PlaceDetailUiState.Success(
                    placeDetail =
                        PlaceDetailUiModel(
                            place =
                                PlaceUiModel(
                                    id = 1,
                                    imageUrl = null,
                                    title = "컹과 주점 '코딩하며 한잔'",
                                    description = "테스트 설명입니다.",
                                    location = "테스트 위치",
                                    category = PlaceCategoryUiModel.FOOD_TRUCK,
                                    isBookmarked = true,
                                    timeTagId = listOf(1),
                                ),
                            notices = emptyList(),
                            host = "테스트",
                            startTime = "09:00",
                            endTime = "18:00",
                            images = listOf(ImageUiModel(id = 1, url = "")),
                        ),
                    waiting = WaitingUiState.Active(totalTeams = 13, estimatedMinutes = 130),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenClosedPreview() {
    FestabookTheme {
        PlaceDetailScreen(
            onBackToPreviousClick = {},
            onShowErrorSnackbar = {},
            uiState =
                PlaceDetailUiState.Success(
                    placeDetail =
                        PlaceDetailUiModel(
                            place =
                                PlaceUiModel(
                                    id = 1,
                                    imageUrl = null,
                                    title = "컹과 주점 '코딩하며 한잔'",
                                    description = "테스트 설명입니다.",
                                    location = "테스트 위치",
                                    category = PlaceCategoryUiModel.FOOD_TRUCK,
                                    isBookmarked = true,
                                    timeTagId = listOf(1),
                                ),
                            notices = emptyList(),
                            host = "테스트",
                            startTime = "09:00",
                            endTime = "18:00",
                            images = listOf(ImageUiModel(id = 1, url = "")),
                        ),
                    waiting = WaitingUiState.Closed(totalTeams = 13),
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenInactivePreview() {
    FestabookTheme {
        PlaceDetailScreen(
            onBackToPreviousClick = {},
            onShowErrorSnackbar = {},
            uiState =
                PlaceDetailUiState.Success(
                    placeDetail =
                        PlaceDetailUiModel(
                            place =
                                PlaceUiModel(
                                    id = 1,
                                    imageUrl = null,
                                    title = "컹과 주점 '코딩하며 한잔'",
                                    description = "테스트 설명입니다.",
                                    location = "테스트 위치",
                                    category = PlaceCategoryUiModel.FOOD_TRUCK,
                                    isBookmarked = true,
                                    timeTagId = listOf(1),
                                ),
                            notices = emptyList(),
                            host = "테스트",
                            startTime = "09:00",
                            endTime = "18:00",
                            images = listOf(ImageUiModel(id = 1, url = "")),
                        ),
                    waiting = WaitingUiState.Inactive,
                ),
        )
    }
}