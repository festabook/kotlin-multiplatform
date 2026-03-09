package com.daedan.festabook.presentation.placeMap.model

import androidx.compose.ui.graphics.Color
import com.daedan.festabook.domain.model.PlaceCategory
import com.daedan.festabook.presentation.placeMap.mapManager.internal.OverlayImageManager
import com.daedan.festabook.presentation.placeMap.platform.OverlayImage
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_bar
import festabookkmp.composeapp.generated.resources.ic_bar_selected
import festabookkmp.composeapp.generated.resources.ic_booth
import festabookkmp.composeapp.generated.resources.ic_booth_selected
import festabookkmp.composeapp.generated.resources.ic_extra
import festabookkmp.composeapp.generated.resources.ic_extra_selected
import festabookkmp.composeapp.generated.resources.ic_food_truck
import festabookkmp.composeapp.generated.resources.ic_food_truck_selected
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
import festabookkmp.composeapp.generated.resources.ic_parking
import festabookkmp.composeapp.generated.resources.ic_parking_selected
import festabookkmp.composeapp.generated.resources.ic_photo_booth
import festabookkmp.composeapp.generated.resources.ic_photo_booth_selected
import festabookkmp.composeapp.generated.resources.ic_primary
import festabookkmp.composeapp.generated.resources.ic_primary_selected
import festabookkmp.composeapp.generated.resources.ic_smoking_area
import festabookkmp.composeapp.generated.resources.ic_smoking_area_selected
import festabookkmp.composeapp.generated.resources.ic_stage
import festabookkmp.composeapp.generated.resources.ic_stage_selected
import festabookkmp.composeapp.generated.resources.ic_toilet
import festabookkmp.composeapp.generated.resources.ic_toilet_selected
import festabookkmp.composeapp.generated.resources.ic_trash
import festabookkmp.composeapp.generated.resources.ic_trash_selected
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
import org.jetbrains.compose.resources.DrawableResource

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

val PlaceCategoryUiModel.Companion.iconResources: List<DrawableResource>
    get() {
        val listOf =
            listOf(
                Res.drawable.ic_food_truck,
                Res.drawable.ic_booth,
                Res.drawable.ic_bar,
                Res.drawable.ic_trash,
                Res.drawable.ic_toilet,
                Res.drawable.ic_smoking_area,
                Res.drawable.ic_primary,
                Res.drawable.ic_parking,
                Res.drawable.ic_stage,
                Res.drawable.ic_photo_booth,
                Res.drawable.ic_extra,
                Res.drawable.ic_food_truck_selected,
                Res.drawable.ic_booth_selected,
                Res.drawable.ic_bar_selected,
                Res.drawable.ic_trash_selected,
                Res.drawable.ic_toilet_selected,
                Res.drawable.ic_smoking_area_selected,
                Res.drawable.ic_primary_selected,
                Res.drawable.ic_parking_selected,
                Res.drawable.ic_stage_selected,
                Res.drawable.ic_photo_booth_selected,
                Res.drawable.ic_extra_selected,
            )
        return listOf
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
        PlaceCategoryUiModel.BOOTH -> getImage(Res.drawable.ic_booth)
        PlaceCategoryUiModel.FOOD_TRUCK -> getImage(Res.drawable.ic_food_truck)
        PlaceCategoryUiModel.TOILET -> getImage(Res.drawable.ic_toilet)
        PlaceCategoryUiModel.BAR -> getImage(Res.drawable.ic_bar)
        PlaceCategoryUiModel.TRASH_CAN -> getImage(Res.drawable.ic_trash)
        PlaceCategoryUiModel.SMOKING_AREA -> getImage(Res.drawable.ic_smoking_area)
        PlaceCategoryUiModel.PRIMARY -> getImage(Res.drawable.ic_primary)
        PlaceCategoryUiModel.PARKING -> getImage(Res.drawable.ic_parking)
        PlaceCategoryUiModel.STAGE -> getImage(Res.drawable.ic_stage)
        PlaceCategoryUiModel.PHOTO_BOOTH -> getImage(Res.drawable.ic_photo_booth)
        PlaceCategoryUiModel.EXTRA -> getImage(Res.drawable.ic_extra)
    }

fun OverlayImageManager.getSelectedIcon(category: PlaceCategoryUiModel): OverlayImage? =
    when (category) {
        PlaceCategoryUiModel.BOOTH -> getImage(Res.drawable.ic_booth_selected)
        PlaceCategoryUiModel.FOOD_TRUCK -> getImage(Res.drawable.ic_food_truck_selected)
        PlaceCategoryUiModel.TOILET -> getImage(Res.drawable.ic_toilet_selected)
        PlaceCategoryUiModel.BAR -> getImage(Res.drawable.ic_bar_selected)
        PlaceCategoryUiModel.TRASH_CAN -> getImage(Res.drawable.ic_trash_selected)
        PlaceCategoryUiModel.SMOKING_AREA -> getImage(Res.drawable.ic_smoking_area_selected)
        PlaceCategoryUiModel.PRIMARY -> getImage(Res.drawable.ic_primary_selected)
        PlaceCategoryUiModel.PARKING -> getImage(Res.drawable.ic_parking_selected)
        PlaceCategoryUiModel.STAGE -> getImage(Res.drawable.ic_stage_selected)
        PlaceCategoryUiModel.PHOTO_BOOTH -> getImage(Res.drawable.ic_photo_booth_selected)
        PlaceCategoryUiModel.EXTRA -> getImage(Res.drawable.ic_extra_selected)
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
