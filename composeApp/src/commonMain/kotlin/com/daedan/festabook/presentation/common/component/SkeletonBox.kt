package com.daedan.festabook.presentation.common.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.daedan.festabook.presentation.theme.FestabookColor

@Composable
fun SkeletonBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
            ),
    )

    val brush =
        Brush.linearGradient(
            colors =
                listOf(
                    FestabookColor.gray500.copy(alpha = 0.6f),
                    FestabookColor.gray500.copy(alpha = 0.2f),
                    FestabookColor.gray500.copy(alpha = 0.6f),
                ),
            start = Offset(translateAnim - 200f, 0f),
            end = Offset(translateAnim, 0f),
        )

    Box(modifier = modifier.background(brush))
}
