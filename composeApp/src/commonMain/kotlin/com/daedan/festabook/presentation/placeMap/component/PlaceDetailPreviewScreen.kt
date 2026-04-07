package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.daedan.festabook.presentation.common.component.FestabookImage
import com.daedan.festabook.presentation.common.component.URLText
import com.daedan.festabook.presentation.common.convertImageUrl
import com.daedan.festabook.presentation.placeMap.intent.state.LoadState
import com.daedan.festabook.presentation.placeMap.model.PlaceCategoryUiModel
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.content_description_booth_image
import festabookkmp.composeapp.generated.resources.content_description_iv_clock
import festabookkmp.composeapp.generated.resources.content_description_iv_host
import festabookkmp.composeapp.generated.resources.content_description_iv_location
import festabookkmp.composeapp.generated.resources.ic_location
import festabookkmp.composeapp.generated.resources.ic_place_detail_clock
import festabookkmp.composeapp.generated.resources.ic_place_detail_host
import festabookkmp.composeapp.generated.resources.place_detail_default_host
import festabookkmp.composeapp.generated.resources.place_detail_default_time
import festabookkmp.composeapp.generated.resources.place_list_default_description
import festabookkmp.composeapp.generated.resources.place_list_default_location
import festabookkmp.composeapp.generated.resources.place_list_default_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlaceDetailPreviewScreen(
    selectedPlace: LoadState<PlaceDetailUiModel>,
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    onClick: (LoadState<PlaceDetailUiModel>) -> Unit = {},
    onBackPress: () -> Unit = {},
) {
    PlaceDetailPreviewBackHandler(enabled = visible) {
        onBackPress()
    }
    PreviewAnimatableBox(
        visible = visible,
        modifier =
            modifier
                .wrapContentSize()
                .clickable { onClick(selectedPlace) },
    ) {
        when (selectedPlace) {
            is LoadState.Success -> {
                PlaceDetailPreviewContent(placeDetail = selectedPlace.value)
            }

            else -> {
                Unit
            }
        }
    }
}

@Composable
private fun PlaceDetailPreviewContent(
    placeDetail: PlaceDetailUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier.padding(
                horizontal = festabookSpacing.paddingScreenGutter,
                vertical = festabookSpacing.previewVerticalPadding,
            ),
    ) {
        PlaceCategoryLabel(
            category = placeDetail.place.category,
        )

        Row(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(top = festabookSpacing.paddingBody1),
                    style = FestabookTypography.displaySmall,
                    text =
                        placeDetail.place.title
                            ?: stringResource(Res.string.place_list_default_title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody3),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_place_detail_clock),
                        contentDescription = stringResource(Res.string.content_description_iv_clock),
                        tint = FestabookColor.gray500,
                    )

                    Text(
                        modifier = Modifier.padding(start = festabookSpacing.paddingBody1),
                        text = formattedDate(placeDetail.startTime, placeDetail.endTime),
                        style = FestabookTypography.bodySmall,
                        color = FestabookColor.gray500,
                    )
                }

                Row(
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location),
                        contentDescription = stringResource(Res.string.content_description_iv_location),
                    )

                    Text(
                        modifier = Modifier.padding(start = festabookSpacing.paddingBody1),
                        text =
                            placeDetail.place.location
                                ?: stringResource(Res.string.place_list_default_location),
                        style = FestabookTypography.bodySmall,
                        color = FestabookColor.gray500,
                    )
                }

                Row(
                    modifier = Modifier.padding(top = festabookSpacing.paddingBody1),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_place_detail_host),
                        contentDescription = stringResource(Res.string.content_description_iv_host),
                    )

                    Text(
                        modifier = Modifier.padding(start = festabookSpacing.paddingBody1),
                        text =
                            placeDetail.host
                                ?: stringResource(Res.string.place_detail_default_host),
                        style = FestabookTypography.bodySmall,
                        color = FestabookColor.gray500,
                    )
                }
            }

            FestabookImage(
                modifier =
                    Modifier
                        .size(festabookSpacing.previewImageSize)
                        .clip(festabookShapes.radius2),
                imageUrl = placeDetail.place.imageUrl.convertImageUrl() ?: "",
                contentDescription = stringResource(Res.string.content_description_booth_image),
            )
        }

        URLText(
            modifier = Modifier.padding(top = festabookSpacing.paddingBody3),
            text =
                placeDetail.place.description
                    ?: stringResource(Res.string.place_list_default_description),
            style = FestabookTypography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun formattedDate(
    startTime: String?,
    endTime: String?,
): String =
    if (startTime == null && endTime == null) {
        stringResource(Res.string.place_detail_default_time)
    } else {
        listOf(startTime, endTime).joinToString(" ~ ")
    }

@Preview
@Composable
private fun PlaceDetailPreviewScreenPreview() {
    FestabookTheme {
        PlaceDetailPreviewScreen(
            modifier =
                Modifier
                    .padding(festabookSpacing.paddingScreenGutter),
            selectedPlace =
                LoadState.Success(
                    value = FAKE_PLACE_DETAIL,
                ),
        )
    }
}

private val FAKE_PLACE =
    PlaceUiModel(
        id = 1,
        imageUrl = null,
        category = PlaceCategoryUiModel.FOOD_TRUCK,
        title = "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트",
        description = "https://onlyfor-me-blog.tistory.com/1190",
        location = null,
        isBookmarked = false,
        timeTagId = listOf(1),
    )

private val FAKE_PLACE_DETAIL =
    PlaceDetailUiModel(
        place = FAKE_PLACE,
        notices = listOf(),
        host = null,
        startTime = null,
        endTime = null,
        images = listOf(),
    )
