package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.explore.model.SearchResultUiModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.daedan.festabook.presentation.theme.FestabookTypography
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.content_description_close
import festabookkmp.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExploreResultItem(
    university: SearchResultUiModel,
    onItemClick: (SearchResultUiModel) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (SearchResultUiModel) -> Unit = {},
    canDelete: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .clickable { onItemClick(university) },
        ) {
            Text(
                text = university.universityName,
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.gray800,
            )
            Text(
                text = university.festivalName,
                style = FestabookTypography.bodySmall,
                color = FestabookColor.gray600,
            )
        }
        if (canDelete) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = stringResource(Res.string.content_description_close),
                tint = FestabookColor.gray600,
                modifier = Modifier.clickable { onDeleteClick(university) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreResultItemPreview() {
    FestabookTheme {
        ExploreResultItem(
            university = SearchResultUiModel(1, "서울시립대학교", "2024 대동제"),
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}
