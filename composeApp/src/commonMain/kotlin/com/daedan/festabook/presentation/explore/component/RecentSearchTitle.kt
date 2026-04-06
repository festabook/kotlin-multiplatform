package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.layout.Arrangement
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
import festabookkmp.composeapp.generated.resources.recent_searches
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecentSearchTitle(
    onClick: () -> Unit,
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
        TextButton(onClick = onClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = stringResource(Res.string.clear_all),
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.gray800,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun RecentSearchTitlePreview() {
    RecentSearchTitle(onClick = {})
}
