package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.content_description_homepage
import festabookkmp.composeapp.generated.resources.content_description_instagram
import festabookkmp.composeapp.generated.resources.ic_homepage
import festabookkmp.composeapp.generated.resources.ic_instagram
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeFestivalInfo(
    festivalName: String,
    festivalDate: String,
    instagramLink: String? = null,
    homepageLink: String? = null,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
    ) {
        Text(
            text = festivalName,
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = festivalDate,
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray500,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
        ) {
            if (instagramLink != null) {
                Icon(
                    modifier =
                        Modifier.clickable {
                            uriHandler.openUri(instagramLink)
                        },
                    painter = painterResource(Res.drawable.ic_instagram),
                    contentDescription = stringResource(Res.string.content_description_instagram),
                    tint = FestabookColor.gray500,
                )
            }
            if (homepageLink != null) {
                Icon(
                    modifier =
                        Modifier.clickable {
                            uriHandler.openUri(homepageLink)
                        },
                    painter = painterResource(Res.drawable.ic_homepage),
                    contentDescription = stringResource(Res.string.content_description_homepage),
                    tint = FestabookColor.gray500,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeFestivalInfoPreview() {
    HomeFestivalInfo(
        festivalName = "2025 가천 Water Festival\n: AQUA WAVE",
        festivalDate = "2025년 10월 15일 - 10월 17일",
    )
}
