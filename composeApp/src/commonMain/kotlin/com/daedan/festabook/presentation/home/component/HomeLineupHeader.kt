package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.home_lineup_title
import festabookkmp.composeapp.generated.resources.home_navigate_to_schedule_desc
import festabookkmp.composeapp.generated.resources.home_navigate_to_schedule_text
import festabookkmp.composeapp.generated.resources.ic_arrow_forward_right
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeLineupHeader(
    onScheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.home_lineup_title),
            style = FestabookTypography.displayMedium,
            color = FestabookColor.black,
        )

        Row(
            modifier =
                Modifier
                    .clickable(
                        onClick = onScheduleClick,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.home_navigate_to_schedule_text),
                style = FestabookTypography.bodySmall,
                color = FestabookColor.gray400,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_arrow_forward_right),
                contentDescription = stringResource(Res.string.home_navigate_to_schedule_desc),
                tint = FestabookColor.gray400,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLineupHeaderPreview() {
    HomeLineupHeader(
        onScheduleClick = {},
    )
}
