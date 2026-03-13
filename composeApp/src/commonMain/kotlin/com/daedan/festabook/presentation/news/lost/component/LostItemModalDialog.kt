package com.daedan.festabook.presentation.news.lost.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.common.component.cardBackground
import com.daedan.festabook.presentation.news.lost.model.LostItemUiStatus
import com.daedan.festabook.presentation.news.lost.model.LostUiModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.modal_lost_item_created_at
import festabookkmp.composeapp.generated.resources.modal_lost_item_location
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val PADDING: Int = 16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostItemModalDialog(
    lostItem: LostUiModel.Item,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PADDING.dp),
            modifier =
                modifier
                    .cardBackground(
                        backgroundColor = FestabookColor.white,
                        borderStroke = 0.dp,
                        borderColor = FestabookColor.white,
                    ).padding(PADDING.dp),
        ) {
            LostItem(url = lostItem.imageUrl)
            Text(
                text =
                    stringResource(
                        Res.string.modal_lost_item_location,
                        lostItem.storageLocation,
                    ),
                color = FestabookColor.gray500,
                style = FestabookTypography.titleMedium,
            )
            Text(
                text =
                    stringResource(
                        Res.string.modal_lost_item_created_at,
                        lostItem.createdAt.toFormattedDateTime(),
                    ),
                color = FestabookColor.gray500,
                style = FestabookTypography.titleMedium,
            )
        }
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
private fun LocalDateTime.toFormattedDateTime(): String {
    val format =
        LocalDateTime.Format {
            byUnicodePattern("yyyy.MM.dd  HH:mm")
        }
    return format(format)
}

@Composable
@Preview
private fun LostItemModalDialogPreview() {
    LostItemModalDialog(
        lostItem =
            LostUiModel.Item(
                lostItemId = 1L,
                imageUrl = "",
                storageLocation = "미소 집",
                status = LostItemUiStatus.PENDING,
                createdAt =
                    LocalDateTime(
                        date = LocalDate(2025, 11, 12),
                        time = LocalTime(11, 12),
                    ),
            ),
        onDismiss = {},
    )
}
