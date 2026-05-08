package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.common.component.FestabookImage
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.home.model.FestivalSponsorUiModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.home_sponsor_title
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val AUTO_SCROLL_DELAY = 4_000L

@Composable
fun HomeSponsorBanner(
    sponsors: List<FestivalSponsorUiModel>,
    modifier: Modifier = Modifier,
) {
    if (sponsors.isEmpty()) return

    val sortedSponsors = remember(sponsors) { sponsors.sortedBy { it.sequence } }
    val initialPage =
        remember(sortedSponsors.size) {
            (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % sortedSponsors.size)
        }
    val pagerState =
        rememberPagerState(
            initialPage = initialPage,
            pageCount = { Int.MAX_VALUE },
        )

    LaunchedEffect(Unit) {
        while (true) {
            delay(AUTO_SCROLL_DELAY)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Column(
        modifier = modifier.padding(horizontal = festabookSpacing.paddingScreenGutter),
    ) {
        Text(
            text = stringResource(Res.string.home_sponsor_title),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )

        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody4))

        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .cardBackground(
                        shape = festabookShapes.radius3,
                    ),
        ) { page ->
            val actualIndex = page % sortedSponsors.size
            FestabookImage(
                imageUrl = sortedSponsors[actualIndex].bannerUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSponsorBannerPreview() {
    HomeSponsorBanner(
        sponsors =
            listOf(
                FestivalSponsorUiModel(1, "sample", 1),
                FestivalSponsorUiModel(2, "sample", 2),
            ),
    )
}
