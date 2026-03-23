package com.daedan.festabook.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.daedan.festabook.presentation.theme.FestabookColor
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_home
import festabookkmp.composeapp.generated.resources.ic_map
import festabookkmp.composeapp.generated.resources.ic_news
import festabookkmp.composeapp.generated.resources.ic_schedule
import festabookkmp.composeapp.generated.resources.ic_setting
import festabookkmp.composeapp.generated.resources.menu_home_title
import festabookkmp.composeapp.generated.resources.menu_map_title
import festabookkmp.composeapp.generated.resources.menu_news_title
import festabookkmp.composeapp.generated.resources.menu_schedule_title
import festabookkmp.composeapp.generated.resources.menu_setting_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class FestabookMainTab(
    val iconResId: DrawableResource,
    val labelResId: StringResource,
    val route: MainTabRoute,
) {
    HOME(
        iconResId = Res.drawable.ic_home,
        labelResId = Res.string.menu_home_title,
        route = MainTabRoute.Home,
    ),
    SCHEDULE(
        iconResId = Res.drawable.ic_schedule,
        labelResId = Res.string.menu_schedule_title,
        route = MainTabRoute.Schedule,
    ),
    PLACE_MAP(
        iconResId = Res.drawable.ic_map,
        labelResId = Res.string.menu_map_title,
        route = MainTabRoute.PlaceMap,
    ),
    NEWS(
        iconResId = Res.drawable.ic_news,
        labelResId = Res.string.menu_news_title,
        route = MainTabRoute.News,
    ),
    SETTING(
        iconResId = Res.drawable.ic_setting,
        labelResId = Res.string.menu_setting_title,
        route = MainTabRoute.Setting,
    ),
    ;

    companion object Defaults {
        @Composable
        fun find(predicate: @Composable (FestabookRoute) -> Boolean) =
            entries.find {
                predicate(it.route)
            }

        val selectedColor
            @Composable
            @ReadOnlyComposable
            get() = FestabookColor.black

        val unselectedColor
            @Composable
            @ReadOnlyComposable
            get() = FestabookColor.gray400
    }
}
