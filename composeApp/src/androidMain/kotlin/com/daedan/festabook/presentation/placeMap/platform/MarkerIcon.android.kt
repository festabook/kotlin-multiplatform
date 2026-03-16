package com.daedan.festabook.presentation.placeMap.platform

import com.daedan.festabook.R

fun MarkerIcon.toResId(): Int =
    when (this) {
        MarkerIcon.IC_BAR -> R.drawable.ic_bar
        MarkerIcon.IC_BAR_SELECTED -> R.drawable.ic_bar_selected
        MarkerIcon.IC_BOOTH -> R.drawable.ic_booth
        MarkerIcon.IC_BOOTH_SELECTED -> R.drawable.ic_booth_selected
        MarkerIcon.IC_FOOD_TRUCK -> R.drawable.ic_food_truck
        MarkerIcon.IC_FOOD_TRUCK_SELECTED -> R.drawable.ic_food_truck_selected
        MarkerIcon.IC_PARKING -> R.drawable.ic_parking
        MarkerIcon.IC_PARKING_SELECTED -> R.drawable.ic_parking_selected
        MarkerIcon.IC_PHOTO_BOOTH -> R.drawable.ic_photo_booth
        MarkerIcon.IC_PHOTO_BOOTH_SELECTED -> R.drawable.ic_photo_booth_selected
        MarkerIcon.IC_PRIMARY -> R.drawable.ic_primary
        MarkerIcon.IC_PRIMARY_SELECTED -> R.drawable.ic_primary_selected
        MarkerIcon.IC_SMOKING_AREA -> R.drawable.ic_smoking_area
        MarkerIcon.IC_SMOKING_AREA_SELECTED -> R.drawable.ic_smoking_area_selected
        MarkerIcon.IC_STAGE -> R.drawable.ic_stage
        MarkerIcon.IC_STAGE_SELECTED -> R.drawable.ic_stage_selected
        MarkerIcon.IC_TOILET -> R.drawable.ic_toilet
        MarkerIcon.IC_TOILET_SELECTED -> R.drawable.ic_toilet_selected
        MarkerIcon.IC_TRASH -> R.drawable.ic_trash
        MarkerIcon.IC_TRASH_SELECTED -> R.drawable.ic_trash_selected
        MarkerIcon.IC_EXTRA -> R.drawable.ic_extra
        MarkerIcon.IC_EXTRA_SELECTED -> R.drawable.ic_extra_selected
    }
