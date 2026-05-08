package com.daedan.festabook.presentation.news.lost.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.daedan.festabook.logging.ScreenViewLogger
import com.daedan.festabook.logging.logClick
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.common.component.PullToRefreshContainer
import com.daedan.festabook.presentation.news.component.NewsItem
import com.daedan.festabook.presentation.news.lost.LostUiState
import com.daedan.festabook.presentation.news.lost.model.LostItemUiStatus
import com.daedan.festabook.presentation.news.lost.model.LostUiModel
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_info
import festabookkmp.composeapp.generated.resources.info
import festabookkmp.composeapp.generated.resources.lost_item_guide
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SPAN_COUNT: Int = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostItemScreen(
    lostUiState: LostUiState,
    onLostGuideClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenViewLogger("LostItemScreen")

    var clickedLostItem by remember { mutableStateOf<LostUiModel.Item?>(null) }
    val scrollState = rememberScrollState()
    val loggedOnRefresh = logClick(identifier = "refresh", screenName = "LostItemScreen", onClick = onRefresh)

    clickedLostItem?.let {
        LostItemModalDialog(
            lostItem = it,
            onDismiss = { clickedLostItem = null },
        )
    }

    PullToRefreshContainer(
        isRefreshing = lostUiState.isRefreshing,
        onRefresh = loggedOnRefresh,
        modifier = modifier,
    ) { graphicsLayer ->
        when (val content = lostUiState.content) {
            LostUiState.Content.InitialLoading -> {
                LoadingStateScreen()
            }

            is LostUiState.Content.Error -> {
//                Timber.w(content.throwable.stackTraceToString())
                ErrorStateScreen(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .then(graphicsLayer)
                            .verticalScroll(scrollState),
                )
            }

            is LostUiState.Content.Success -> {
                LostItemContent(
                    lostItems = content.lostItems,
                    onLostGuideClick = onLostGuideClick,
                    onLostItemClick = { clickedLostItem = it },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .then(graphicsLayer),
                )
            }
        }
    }
}

@Composable
private fun LostItemContent(
    lostItems: List<LostUiModel>,
    onLostGuideClick: () -> Unit,
    onLostItemClick: (LostUiModel.Item) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val isLostItemEmpty = lostItems.none { it is LostUiModel.Item }
        if (isLostItemEmpty) {
            EmptyStateScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(SPAN_COUNT),
            contentPadding =
                PaddingValues(
                    top = festabookSpacing.paddingBody2,
                    bottom = festabookSpacing.paddingBody2,
                ),
            verticalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
            horizontalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
        ) {
            item(span = { GridItemSpan(SPAN_COUNT) }) {
                val guide = lostItems.firstOrNull() as? LostUiModel.Guide
                guide?.let {
                    val loggedOnLostGuideClick =
                        logClick(
                            identifier = "lost_guide_click",
                            screenName = "LostItemScreen",
                            onClick = onLostGuideClick,
                        )
                    NewsItem(
                        title = stringResource(Res.string.lost_item_guide),
                        description = it.description,
                        isExpanded = it.isExpanded,
                        onclick = loggedOnLostGuideClick,
                        icon =
                            {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_info),
                                    contentDescription = stringResource(Res.string.info),
                                )
                            },
                    )
                }
            }
            items(
                items = lostItems.drop(1).filterIsInstance<LostUiModel.Item>(),
                key = { lostItem -> lostItem.lostItemId },
            ) { lostItem ->
                val loggedOnLostItemClick =
                    logClick(
                        identifier = "lost_item_click",
                        screenName = "LostItemScreen",
                        extraParam = mapOf("lost_item_id" to lostItem.lostItemId.toString()),
                        onClick = { onLostItemClick(lostItem) },
                    )
                LostItem(
                    url = lostItem.imageUrl,
                    onLostItemClick = loggedOnLostItemClick,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LostItemContentPreview() {
    val dummyLostList: List<LostUiModel> =
        listOf(
            LostUiModel.Guide(
                description = "운영 시간: 09:00 ~ 18:00",
                isExpanded = true,
            ),
            LostUiModel.Item(
                lostItemId = 1L,
                imageUrl = "https://i.imgur.com/Zblctu7.png",
                storageLocation = "1층 안내데스크",
                status = LostItemUiStatus.PENDING,
                createdAt =
                    LocalDateTime(
                        date = LocalDate(2025, 11, 12),
                        time = LocalTime(11, 12),
                    ),
            ),
            LostUiModel.Item(
                lostItemId = 2L,
                imageUrl = "https://i.imgur.com/Zblctu7.png",
                storageLocation = "2층 분실물 보관함",
                status = LostItemUiStatus.PENDING,
                createdAt =
                    LocalDateTime(
                        date = LocalDate(2025, 11, 12),
                        time = LocalTime(11, 12),
                    ),
            ),
        )
    LostItemContent(
        lostItems = dummyLostList,
        onLostGuideClick = { },
        modifier = Modifier.fillMaxSize(),
        onLostItemClick = { },
    )
}
