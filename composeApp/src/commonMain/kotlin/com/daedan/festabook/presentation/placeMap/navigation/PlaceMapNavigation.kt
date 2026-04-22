package com.daedan.festabook.presentation.placeMap.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.daedan.festabook.presentation.NotificationPermissionManager
import com.daedan.festabook.presentation.main.FestabookRoute
import com.daedan.festabook.presentation.main.MainTabRoute
import com.daedan.festabook.presentation.placeMap.model.PlaceUiModel
import com.daedan.festabook.presentation.placeMap.placeDetail.PlaceDetailViewModel
import com.daedan.festabook.presentation.placeMap.placeDetail.component.PlaceDetailRoute
import com.daedan.festabook.presentation.placeMap.placeDetail.model.PlaceDetailUiModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.WaitingRegisterViewModel
import com.daedan.festabook.presentation.placeMap.waitingRegister.component.WaitingRegisterRoute
import com.daedan.festabook.presentation.setting.SettingViewModel
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

fun NavGraphBuilder.placeMapNavGraph(
    innerPadding: PaddingValues,
    settingViewModel: SettingViewModel,
    notificationPermissionManagerFactory: NotificationPermissionManager.Factory,
    onBackToPreviousClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    onNavigateToAddWaitingInfo: (Long) -> Unit,
    onNavigateToPhoneRegistration: (Long) -> Unit,
    onNavigateToMyWaiting: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<MainTabRoute.PlaceMap>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
    }

    composable<FestabookRoute.PlaceDetail>(
        typeMap =
            mapOf(
                typeOf<PlaceDetailUiModel?>() to defaultNavType<PlaceDetailUiModel?>(),
                typeOf<PlaceUiModel?>() to defaultNavType<PlaceUiModel?>(),
            ),
        enterTransition = {
            slideInVertically(initialOffsetY = { it / INITIAL_OFFSET_CONSTANT }) + fadeIn()
        },
        exitTransition = {
            slideOutVertically(targetOffsetY = { it / INITIAL_OFFSET_CONSTANT }) + fadeOut()
        },
    ) { backStackEntry ->
        val route = backStackEntry.toRoute<FestabookRoute.PlaceDetail>()
        val viewModel =
            assistedMetroViewModel<PlaceDetailViewModel>(
                extras =
                    MutableCreationExtras().apply {
                        set(PlaceDetailViewModel.PlaceIdKey, route.placeId)
                    },
            )

        PlaceDetailRoute(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .graphicsLayer(
                        compositingStrategy = CompositingStrategy.Offscreen,
                        clip = true,
                    ),
            viewModel = viewModel,
            onBackToPreviousClick = onBackToPreviousClick,
            onShowErrorSnackbar = onShowErrorSnackbar,
            onNavigateToWaitingRegister = onNavigateToAddWaitingInfo,
            onNavigateToMyWaiting = onNavigateToMyWaiting,
        )
    }

    composable<FestabookRoute.WaitingRegister>(
        enterTransition = {
            slideInVertically(initialOffsetY = { it / 10 }) + fadeIn()
        },
        exitTransition = {
            slideOutVertically(targetOffsetY = { it / 10 }) + fadeOut()
        },
    ) { backStackEntry ->
        val route = backStackEntry.toRoute<FestabookRoute.WaitingRegister>()
        val viewModel =
            assistedMetroViewModel<WaitingRegisterViewModel>(
                extras =
                    MutableCreationExtras().apply {
                        set(WaitingRegisterViewModel.PlaceIdKey, route.placeId)
                    },
            )

        WaitingRegisterRoute(
            viewModel = viewModel,
            settingViewModel = settingViewModel,
            modifier = Modifier.padding(innerPadding),
            onBackToPreviousClick = onBackToPreviousClick,
            onShowErrorSnackbar = onShowErrorSnackbar,
            onShowSnackbar = onShowSnackbar,
            onNavigateToPhoneRegistration = { onNavigateToPhoneRegistration(route.placeId) },
            onNavigateToMyWaiting = onNavigateToMyWaiting,
            notificationPermissionManagerFactory = notificationPermissionManagerFactory,
        )
    }
}

private const val INITIAL_OFFSET_CONSTANT = 10

private inline fun <reified T> defaultNavType() =
    object : NavType<T>(isNullableAllowed = true) {
        override fun put(
            bundle: SavedState,
            key: String,
            value: T,
        ) {
            bundle.write {
                putString(key, Json.encodeToString(value))
            }
        }

        override fun get(
            bundle: SavedState,
            key: String,
        ): T? =
            bundle.read {
                getStringOrNull(key)?.let { Json.decodeFromString<T>(it) }
            }

        override fun parseValue(value: String): T = Json.decodeFromString(value)

        override fun serializeAsValue(value: T): String = Json.encodeToString(value)
    }
