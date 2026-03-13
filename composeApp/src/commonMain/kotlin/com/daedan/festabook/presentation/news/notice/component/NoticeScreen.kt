package com.daedan.festabook.presentation.news.notice.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.common.component.PullToRefreshContainer
import com.daedan.festabook.presentation.news.component.NewsItem
import com.daedan.festabook.presentation.news.notice.NoticeUiState
import com.daedan.festabook.presentation.news.notice.NoticeUiState.Companion.DEFAULT_POSITION
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_pin
import festabookkmp.composeapp.generated.resources.ic_speaker
import festabookkmp.composeapp.generated.resources.iv_pin
import festabookkmp.composeapp.generated.resources.iv_speaker
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticeScreen(
    uiState: NoticeUiState,
    onNoticeClick: (NoticeUiModel) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    PullToRefreshContainer(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
    ) { graphicsLayer ->
        when (val content = uiState.content) {
            NoticeUiState.Content.InitialLoading -> {
                LoadingStateScreen()
            }

            is NoticeUiState.Content.Error -> {
//                Timber.e(content.throwable.stackTraceToString())
                ErrorStateScreen(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .then(graphicsLayer)
                            .verticalScroll(scrollState),
                )
            }

            is NoticeUiState.Content.Success -> {
                NoticeContent(
                    scrollState = scrollState,
                    notices = content.notices,
                    expandPosition = content.expandPosition,
                    onNoticeClick = onNoticeClick,
                    modifier = Modifier.then(graphicsLayer),
                )
            }
        }
    }
}

@Composable
private fun NoticeContent(
    scrollState: ScrollState,
    notices: List<NoticeUiModel>,
    onNoticeClick: (NoticeUiModel) -> Unit,
    modifier: Modifier = Modifier,
    expandPosition: Int = DEFAULT_POSITION,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(expandPosition) {
        listState.animateScrollToItem(expandPosition)
    }
    if (notices.isEmpty()) {
        EmptyStateScreen(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        )
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding =
                PaddingValues(
                    top = festabookSpacing.paddingBody2,
                    bottom = festabookSpacing.paddingBody2,
                ),
            verticalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
        ) {
            items(
                items = notices,
                key = { notice -> notice.id },
            ) { notice ->
                NewsItem(
                    title = notice.title,
                    description = notice.content,
                    isExpanded = notice.isExpanded,
                    onclick = { onNoticeClick(notice) },
                    icon = {
                        if (notice.isPinned) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_pin),
                                contentDescription = stringResource(Res.string.iv_pin),
                            )
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.ic_speaker),
                                contentDescription = stringResource(Res.string.iv_speaker),
                            )
                        }
                    },
                    createdAt = notice.createdAt.toFormattedDateTime(),
                )
            }
        }
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toFormattedDateTime(): String {
    val format =
        LocalDateTime.Format {
            byUnicodePattern("MM/dd HH:mm")
        }
    return format(format)
}

@Preview
@Composable
private fun NoticeScreenPreview() {
    NoticeScreen(
        uiState =
            NoticeUiState(
                content =
                    NoticeUiState.Content.Success(
                        notices = emptyList(),
                        expandPosition = 0,
                    ),
            ),
        onNoticeClick = { },
        onRefresh = {},
    )
}
