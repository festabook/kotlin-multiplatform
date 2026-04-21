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
    data class WaitingRegister(
        val placeId: Long,
    ) : FestabookRoute

    @Serializable
    data object Explore : FestabookRoute

    data class Main(
        val pendingAnnouncementId: Long? = null,
    ) : FestabookRoute

    @Serializable
    data class Festating(
        val festivalId: Long,
    ) : FestabookRoute

    @Serializable
    data class AddWaitingInfo(
        val placeId: Long? = null,
    ) : FestabookRoute
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
