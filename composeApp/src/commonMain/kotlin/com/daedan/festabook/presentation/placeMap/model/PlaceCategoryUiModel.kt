package com.daedan.festabook.presentation.placeMap.model

import androidx.compose.ui.graphics.Color
import com.daedan.festabook.domain.model.PlaceCategory
import com.daedan.festabook.presentation.placeMap.mapManager.internal.OverlayImageManager
import com.daedan.festabook.presentation.placeMap.platform.MarkerIcon
import com.daedan.festabook.presentation.placeMap.platform.OverlayImage
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_map_category_bar
import festabookkmp.composeapp.generated.resources.ic_map_category_booth
import festabookkmp.composeapp.generated.resources.ic_map_category_extra
import festabookkmp.composeapp.generated.resources.ic_map_category_food_truck
import festabookkmp.composeapp.generated.resources.ic_map_category_parking
import festabookkmp.composeapp.generated.resources.ic_map_category_photo_booth
import festabookkmp.composeapp.generated.resources.ic_map_category_primary
import festabookkmp.composeapp.generated.resources.ic_map_category_smoking
import festabookkmp.composeapp.generated.resources.ic_map_category_stage
import festabookkmp.composeapp.generated.resources.ic_map_category_toilet
import festabookkmp.composeapp.generated.resources.ic_map_category_trash
import festabookkmp.composeapp.generated.resources.map_category_bar
import festabookkmp.composeapp.generated.resources.map_category_booth
import festabookkmp.composeapp.generated.resources.map_category_extra
import festabookkmp.composeapp.generated.resources.map_category_food_truck
import festabookkmp.composeapp.generated.resources.map_category_parking
import festabookkmp.composeapp.generated.resources.map_category_photo_booth
import festabookkmp.composeapp.generated.resources.map_category_primary
import festabookkmp.composeapp.generated.resources.map_category_smoking_area
import festabookkmp.composeapp.generated.resources.map_category_stage
import festabookkmp.composeapp.generated.resources.map_category_toilet
import festabookkmp.composeapp.generated.resources.map_category_trash

enum class PlaceCategoryUiModel {
    FOOD_TRUCK,
    BOOTH,
    BAR,

    STAGE,
    PHOTO_BOOTH,
    PRIMARY,

    EXTRA,
    PARKING,
    TOILET,

    SMOKING_AREA,

    TRASH_CAN,
    ;

    companion object {
        val SECONDARY_CATEGORIES =
            listOf(
                TRASH_CAN,
                TOILET,
                SMOKING_AREA,
                PARKING,
                PRIMARY,
                STAGE,
                PHOTO_BOOTH,
                EXTRA,
            )
    }
}

fun PlaceCategoryUiModel.getLabelColor() =
    when (this) {
        PlaceCategoryUiModel.BOOTH -> Color(0xFF0094FF)
        PlaceCategoryUiModel.FOOD_TRUCK -> Color(0xFF00AB40)
        PlaceCategoryUiModel.BAR -> Color(0xFFFF9D00)
        else -> Color.Unspecified
    }

fun OverlayImageManager.getNormalIcon(category: PlaceCategoryUiModel): OverlayImage? =
    when (category) {
        PlaceCategoryUiModel.BOOTH -> getImage(MarkerIcon.IC_BOOTH)
        PlaceCategoryUiModel.FOOD_TRUCK -> getImage(MarkerIcon.IC_FOOD_TRUCK)
        PlaceCategoryUiModel.TOILET -> getImage(MarkerIcon.IC_TOILET)
        PlaceCategoryUiModel.BAR -> getImage(MarkerIcon.IC_BAR)
        PlaceCategoryUiModel.TRASH_CAN -> getImage(MarkerIcon.IC_TRASH)
        PlaceCategoryUiModel.SMOKING_AREA -> getImage(MarkerIcon.IC_SMOKING_AREA)
        PlaceCategoryUiModel.PRIMARY -> getImage(MarkerIcon.IC_PRIMARY)
        PlaceCategoryUiModel.PARKING -> getImage(MarkerIcon.IC_PARKING)
        PlaceCategoryUiModel.STAGE -> getImage(MarkerIcon.IC_STAGE)
        PlaceCategoryUiModel.PHOTO_BOOTH -> getImage(MarkerIcon.IC_PHOTO_BOOTH)
        PlaceCategoryUiModel.EXTRA -> getImage(MarkerIcon.IC_EXTRA)
    }

fun OverlayImageManager.getSelectedIcon(category: PlaceCategoryUiModel): OverlayImage? =
    when (category) {
        PlaceCategoryUiModel.BOOTH -> getImage(MarkerIcon.IC_BOOTH_SELECTED)
        PlaceCategoryUiModel.FOOD_TRUCK -> getImage(MarkerIcon.IC_FOOD_TRUCK_SELECTED)
        PlaceCategoryUiModel.TOILET -> getImage(MarkerIcon.IC_TOILET_SELECTED)
        PlaceCategoryUiModel.BAR -> getImage(MarkerIcon.IC_BAR_SELECTED)
        PlaceCategoryUiModel.TRASH_CAN -> getImage(MarkerIcon.IC_TRASH_SELECTED)
        PlaceCategoryUiModel.SMOKING_AREA -> getImage(MarkerIcon.IC_SMOKING_AREA_SELECTED)
        PlaceCategoryUiModel.PRIMARY -> getImage(MarkerIcon.IC_PRIMARY_SELECTED)
        PlaceCategoryUiModel.PARKING -> getImage(MarkerIcon.IC_PARKING_SELECTED)
        PlaceCategoryUiModel.STAGE -> getImage(MarkerIcon.IC_STAGE_SELECTED)
        PlaceCategoryUiModel.PHOTO_BOOTH -> getImage(MarkerIcon.IC_PHOTO_BOOTH_SELECTED)
        PlaceCategoryUiModel.EXTRA -> getImage(MarkerIcon.IC_EXTRA_SELECTED)
    }

fun PlaceCategoryUiModel.getIconId() =
    when (this) {
        PlaceCategoryUiModel.BOOTH -> Res.drawable.ic_map_category_booth
        PlaceCategoryUiModel.FOOD_TRUCK -> Res.drawable.ic_map_category_food_truck
        PlaceCategoryUiModel.TOILET -> Res.drawable.ic_map_category_toilet
        PlaceCategoryUiModel.BAR -> Res.drawable.ic_map_category_bar
        PlaceCategoryUiModel.TRASH_CAN -> Res.drawable.ic_map_category_trash
        PlaceCategoryUiModel.SMOKING_AREA -> Res.drawable.ic_map_category_smoking
        PlaceCategoryUiModel.PRIMARY -> Res.drawable.ic_map_category_primary
        PlaceCategoryUiModel.PARKING -> Res.drawable.ic_map_category_parking
        PlaceCategoryUiModel.STAGE -> Res.drawable.ic_map_category_stage
        PlaceCategoryUiModel.PHOTO_BOOTH -> Res.drawable.ic_map_category_photo_booth
        PlaceCategoryUiModel.EXTRA -> Res.drawable.ic_map_category_extra
    }

fun PlaceCategoryUiModel.getTextId() =
    when (this) {
        PlaceCategoryUiModel.BOOTH -> Res.string.map_category_booth
        PlaceCategoryUiModel.FOOD_TRUCK -> Res.string.map_category_food_truck
        PlaceCategoryUiModel.TOILET -> Res.string.map_category_toilet
        PlaceCategoryUiModel.BAR -> Res.string.map_category_bar
        PlaceCategoryUiModel.TRASH_CAN -> Res.string.map_category_trash
        PlaceCategoryUiModel.SMOKING_AREA -> Res.string.map_category_smoking_area
        PlaceCategoryUiModel.PRIMARY -> Res.string.map_category_primary
        PlaceCategoryUiModel.PARKING -> Res.string.map_category_parking
        PlaceCategoryUiModel.STAGE -> Res.string.map_category_stage
        PlaceCategoryUiModel.PHOTO_BOOTH -> Res.string.map_category_photo_booth
        PlaceCategoryUiModel.EXTRA -> Res.string.map_category_extra
    }

fun PlaceCategory.toUiModel() =
    when (this) {
        PlaceCategory.BOOTH -> PlaceCategoryUiModel.BOOTH
        PlaceCategory.FOOD_TRUCK -> PlaceCategoryUiModel.FOOD_TRUCK
        PlaceCategory.TOILET -> PlaceCategoryUiModel.TOILET
        PlaceCategory.BAR -> PlaceCategoryUiModel.BAR
        PlaceCategory.TRASH_CAN -> PlaceCategoryUiModel.TRASH_CAN
        PlaceCategory.SMOKING_AREA -> PlaceCategoryUiModel.SMOKING_AREA
        PlaceCategory.PARKING -> PlaceCategoryUiModel.PARKING
        PlaceCategory.PRIMARY -> PlaceCategoryUiModel.PRIMARY
        PlaceCategory.STAGE -> PlaceCategoryUiModel.STAGE
        PlaceCategory.PHOTO_BOOTH -> PlaceCategoryUiModel.PHOTO_BOOTH
        PlaceCategory.EXTRA -> PlaceCategoryUiModel.EXTRA
    }
