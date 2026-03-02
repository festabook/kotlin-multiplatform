package com.daedan.festabook.presentation.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.loading_animation_content_description
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoadingStateScreen(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
) {
    val composition by rememberLottieComposition {
        val jsonString = Res.readBytes("files/loading.json").decodeToString()
        LottieCompositionSpec.JsonString(jsonString)
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever,
        isPlaying = isPlaying,
    )
    Image(
        modifier = modifier.fillMaxSize(),
        painter =
            rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
        contentDescription = stringResource(Res.string.loading_animation_content_description),
    )
}
