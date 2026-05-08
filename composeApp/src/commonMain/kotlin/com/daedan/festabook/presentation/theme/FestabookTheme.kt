package com.daedan.festabook.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.daedan.festabook.presentation.platform.rememberAppGraph
import com.skydoves.landscapist.coil3.LocalCoilImageLoader
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

private val LightColorScheme
    @Composable
    get() =
        lightColorScheme(
            background = FestabookColor.white,
        )

@Composable
fun FestabookTheme(content: @Composable () -> Unit) {
    val spacing = FestabookSpacing()
    val shapes = FestabookShapes()
    val color = FestabookColorPalette()
    val typography = FestabookTypographies
    val appGraph = rememberAppGraph()
    val platformContext = LocalPlatformContext.current

    setSingletonImageLoaderFactory { context ->
        ImageLoader
            .Builder(context)
            .crossfade(true)
            .build()
    }

    CompositionLocalProvider(
        LocalSpacing provides spacing,
        LocalShapes provides shapes,
        LocalColor provides color,
        LocalTypography provides typography,
        LocalMetroViewModelFactory provides appGraph.metroViewModelFactory,
        LocalCoilImageLoader provides SingletonImageLoader.get(platformContext),
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = FestabookTypography,
            content = content,
        )
    }
}
