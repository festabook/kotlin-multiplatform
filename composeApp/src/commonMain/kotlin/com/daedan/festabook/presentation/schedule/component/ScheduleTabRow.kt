package com.daedan.festabook.presentation.schedule.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.schedule.model.ScheduleDateUiModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.friday
import festabookkmp.composeapp.generated.resources.monday
import festabookkmp.composeapp.generated.resources.saturday
import festabookkmp.composeapp.generated.resources.sunday
import festabookkmp.composeapp.generated.resources.thursday
import festabookkmp.composeapp.generated.resources.tuesday
import festabookkmp.composeapp.generated.resources.wednesday
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScheduleTabRow(
    pageState: PagerState,
    scope: CoroutineScope,
    dates: List<ScheduleDateUiModel>,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = if (dates.isEmpty()) 0 else pageState.currentPage

    PrimaryScrollableTabRow(
        edgePadding = festabookSpacing.paddingScreenGutter,
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        indicator = {
            ScheduleTabIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedIndex)
            )
        },
        divider = {},
        modifier = modifier,
    ) {
        if (dates.isEmpty()) {
            Tab(
                selected = true,
                unselectedContentColor = FestabookColor.gray500,
                selectedContentColor = MaterialTheme.colorScheme.background,
                onClick = {},
                text = { Text("-") },
            )
        } else {
            dates.forEachIndexed { index, scheduleDate ->
                Tab(
                    selected = selectedIndex == index,
                    unselectedContentColor = FestabookColor.gray500,
                    selectedContentColor = MaterialTheme.colorScheme.background,
                    onClick = { scope.launch { pageState.animateScrollToPage(index) } },
                    text = { Text(scheduleDate.date.toFormattedDate()) },
                )
            }
        }
    }
}

@Composable
private fun ScheduleTabIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .padding(festabookSpacing.paddingBody1)
                .fillMaxSize()
                .cardBackground(
                    backgroundColor = FestabookColor.black,
                    borderStroke = 0.dp,
                    borderColor = FestabookColor.black,
                    shape = festabookShapes.radius4,
                ).zIndex(-1f),
    )
}

@Composable
private fun LocalDate.toFormattedDate(): String {
    val dayName =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> stringResource(Res.string.monday)
            DayOfWeek.TUESDAY -> stringResource(Res.string.tuesday)
            DayOfWeek.WEDNESDAY -> stringResource(Res.string.wednesday)
            DayOfWeek.THURSDAY -> stringResource(Res.string.thursday)
            DayOfWeek.FRIDAY -> stringResource(Res.string.friday)
            DayOfWeek.SATURDAY -> stringResource(Res.string.saturday)
            DayOfWeek.SUNDAY -> stringResource(Res.string.sunday)
        }

    return "${month.number}/$day ($dayName)"
}

@Preview
@Composable
private fun ScheduleTabRowPreview() {
    ScheduleTabRow(
        pageState = rememberPagerState { 5 },
        scope = rememberCoroutineScope(),
        dates =
            listOf(
                ScheduleDateUiModel(
                    id = 1,
                    date = LocalDate(2025, 11, 12),
                ),
                ScheduleDateUiModel(
                    id = 2,
                    date = LocalDate(2025, 11, 13),
                ),
                ScheduleDateUiModel(
                    id = 3,
                    date = LocalDate(2025, 11, 13),
                ),
                ScheduleDateUiModel(
                    id = 4,
                    date = LocalDate(2025, 11, 13),
                ),
                ScheduleDateUiModel(
                    id = 5,
                    date = LocalDate(2025, 11, 13),
                ),
            ),
    )
}
