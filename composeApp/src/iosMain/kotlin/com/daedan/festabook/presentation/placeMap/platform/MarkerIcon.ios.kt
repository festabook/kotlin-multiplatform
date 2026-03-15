package com.daedan.festabook.presentation.placeMap.platform

fun MarkerIcon.toName(): String =
    when (this) {
        MarkerIcon.IC_BAR -> "ic_bar"
        MarkerIcon.IC_BAR_SELECTED -> "ic_bar_selected"
        MarkerIcon.IC_BOOTH -> "ic_booth"
        MarkerIcon.IC_BOOTH_SELECTED -> "ic_booth_selected"
        MarkerIcon.IC_FOOD_TRUCK -> "ic_food_truck"
        MarkerIcon.IC_FOOD_TRUCK_SELECTED -> "ic_food_truck_selected"
        MarkerIcon.IC_PARKING -> "ic_parking"
        MarkerIcon.IC_PARKING_SELECTED -> "ic_parking_selected"
        MarkerIcon.IC_PHOTO_BOOTH -> "ic_photo_booth"
        MarkerIcon.IC_PHOTO_BOOTH_SELECTED -> "ic_photo_booth_selected"
        MarkerIcon.IC_PRIMARY -> "ic_primary"
        MarkerIcon.IC_PRIMARY_SELECTED -> "ic_primary_selected"
        MarkerIcon.IC_SMOKING_AREA -> "ic_smoking_area"
        MarkerIcon.IC_SMOKING_AREA_SELECTED -> "ic_smoking_area_selected"
        MarkerIcon.IC_STAGE -> "ic_stage"
        MarkerIcon.IC_STAGE_SELECTED -> "ic_stage_selected"
        MarkerIcon.IC_TOILET -> "ic_toilet"
        MarkerIcon.IC_TOILET_SELECTED -> "ic_toilet_selected"
        MarkerIcon.IC_TRASH -> "ic_trash"
        MarkerIcon.IC_TRASH_SELECTED -> "ic_trash_selected"
        MarkerIcon.IC_EXTRA -> "ic_extra"
        MarkerIcon.IC_EXTRA_SELECTED -> "ic_extra_selected"
    }
