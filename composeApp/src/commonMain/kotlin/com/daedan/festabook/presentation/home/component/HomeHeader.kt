package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daedan.festabook.presentation.common.throttleClick
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.home_navigate_to_explore_desc
import festabookkmp.composeapp.generated.resources.ic_dropdown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeHeader(
    universityName: String,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.throttleClick { onTitleClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = universityName,
                style =
                    FestabookTypography.displayLarge.copy(
                        platformStyle = HomeTextStyle,
                        lineHeight = 34.sp,
                    ),
                color = FestabookColor.black,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_dropdown),
                tint = FestabookColor.black,
                contentDescription = stringResource(Res.string.home_navigate_to_explore_desc),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    HomeHeader(
        universityName = "가천대학교",
        onTitleClick = {},
    )
}
