package com.daedan.festabook.presentation.common

import com.daedan.festabook.BuildKonfig

fun String?.convertImageUrl() =
    if (this != null && this.startsWith("/images/")) {
        BuildKonfig.FESTABOOK_IMAGE_URL + this
    } else {
        this
    }
