package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.clear_all
import festabookkmp.composeapp.generated.resources.no_recent_searches_message
import festabookkmp.composeapp.generated.resources.recent_searches
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecentSearchMessageView(
    onClearAllClick: () -> Unit,
    isExist: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isExist) {
        RecentSearchTitle(modifier = modifier, onClearAllClick = onClearAllClick)
    } else {
        NoRecentSearchTitle(modifier = modifier)
    }
}

@Composable
private fun RecentSearchTitle(
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(Res.string.recent_searches),
            style = FestabookTypography.titleLarge,
            color = FestabookColor.gray800,
        )
        TextButton(onClick = onClearAllClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = stringResource(Res.string.clear_all),
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.gray800,
            )
        }
    }
}

@Composable
private fun NoRecentSearchTitle(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(Res.string.no_recent_searches_message),
            style = FestabookTypography.bodyLarge,
            color = FestabookColor.gray800,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecentSearchTitlePreview() {
    RecentSearchMessageView(onClearAllClick = {}, isExist = false)
}
