package com.daedan.festabook.presentation.news.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.daedan.festabook.presentation.news.NewsTab
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NewsTabRow(
    pageState: PagerState,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    PrimaryTabRow(
        selectedTabIndex = pageState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        indicator = {
            PrimaryIndicator(
                color = FestabookColor.black,
                width = Dp.Unspecified,
                modifier = Modifier.tabIndicatorOffset(pageState.currentPage),
            )
        },
        modifier = modifier,
    ) {
        NewsTab.entries.forEachIndexed { index, title ->
            Tab(
                selected = pageState.currentPage == index,
                unselectedContentColor = FestabookColor.gray500,
                selectedContentColor = FestabookColor.black,
                onClick = { scope.launch { pageState.animateScrollToPage(index) } },
                text = { Text(text = stringResource(title.tabNameRes)) },
            )
        }
    }
}

@Composable
@Preview
private fun NewsTabRowPreview() {
    FestabookTheme {
        NewsTabRow(
            pageState = rememberPagerState { 3 },
            scope = rememberCoroutineScope(),
        )
    }
}
