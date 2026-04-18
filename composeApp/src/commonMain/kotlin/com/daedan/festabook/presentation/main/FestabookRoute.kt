package com.daedan.festabook.presentation.main

import kotlinx.serialization.Serializable

@Serializable
sealed interface FestabookRoute {
    @Serializable
    data object Splash : FestabookRoute

    @Serializable
    data class PlaceDetail(
        val placeId: Long,
    ) : FestabookRoute

    @Serializable
    data object Explore : FestabookRoute

    @Serializable
    data object Main : FestabookRoute

    @Serializable
    data object AddWaitingInfo : FestabookRoute
}

@Serializable
sealed interface MainTabRoute : FestabookRoute {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Schedule : MainTabRoute

    @Serializable
    data object PlaceMap : MainTabRoute

    @Serializable
    data object News : MainTabRoute

    @Serializable
    data object Setting : MainTabRoute
}
