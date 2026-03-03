package com.daedan.festabook.presentation.schedule.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.daedan.festabook.presentation.schedule.model.ScheduleEventUiModel
import com.daedan.festabook.presentation.schedule.model.ScheduleEventUiStatus
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.festabookSpacing
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.ExperimentalCompottieApi
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.dynamic.rememberLottieDynamicProperties
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScheduleEventItem(
    composition: LottieComposition?,
    scheduleEvent: ScheduleEventUiModel,
    modifier: Modifier = Modifier,
) {
    val props = lottieTimeLineCircleProps(scheduleEvent.status)
    val dynamicProperties = rememberScheduleEventDynamicProperties(props)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever,
        isPlaying = scheduleEvent.status != ScheduleEventUiStatus.COMPLETED,
    )

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter =
                rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                    dynamicProperties = dynamicProperties,
                ),
            contentDescription = null,
            modifier = Modifier.size(festabookSpacing.paddingBody4 * 4),
        )

        ScheduleEventCard(scheduleEvent = scheduleEvent)
    }
}

@Composable
@Preview
private fun ScheduleEventItemPreview() {
    ScheduleEventItem(
        composition = null,
        scheduleEvent =
            ScheduleEventUiModel(
                id = 1,
                status = ScheduleEventUiStatus.ONGOING,
                startTime = "9:00",
                endTime = "18:00",
                title = "동아리 버스킹 공연",
                location = "운동장",
            ),
    )
}

@OptIn(ExperimentalCompottieApi::class)
@Composable
private fun rememberScheduleEventDynamicProperties(props: LottieTimeLineCircleProps) =
    rememberLottieDynamicProperties {
        shapeLayer(*props.centerKeyPath.toTypedArray()) {
            fill {
                color { props.centerColor }
            }
        }

        shapeLayer(*props.outerKeyPath.toTypedArray()) {
            fill {
                color { props.outerColor }
                transform {
                    opacity { props.outerOpacity }
                }
            }
        }

        shapeLayer(*props.innerKeyPath.toTypedArray()) {
            fill {
                color { props.innerColor }
                transform {
                    opacity { props.innerOpacity }
                }
            }
        }
    }

@Composable
private fun lottieTimeLineCircleProps(status: ScheduleEventUiStatus): LottieTimeLineCircleProps =
    when (status) {
        ScheduleEventUiStatus.UPCOMING -> {
            LottieTimeLineCircleProps(
                centerColor = FestabookColor.accentGreen,
                outerOpacity = 0f,
                innerOpacity = 1f,
                outerColor = FestabookColor.accentGreen,
                innerColor = FestabookColor.accentGreen,
            )
        }

        ScheduleEventUiStatus.ONGOING -> {
            LottieTimeLineCircleProps(
                centerColor = FestabookColor.accentBlue,
                outerOpacity = 1f,
                innerOpacity = 1f,
                outerColor = FestabookColor.accentBlue,
                innerColor = FestabookColor.accentBlue,
            )
        }

        ScheduleEventUiStatus.COMPLETED -> {
            LottieTimeLineCircleProps(
                centerColor = FestabookColor.gray300,
                outerOpacity = 0f,
                innerOpacity = 0f,
                outerColor = FestabookColor.gray300,
                innerColor = FestabookColor.gray300,
            )
        }
    }

data class LottieTimeLineCircleProps(
    val centerColor: Color,
    val outerOpacity: Float,
    val innerOpacity: Float,
    val outerColor: Color,
    val innerColor: Color,
    val centerKeyPath: List<String> = listOf("centerCircle", "**", "Fill 1"),
    val outerKeyPath: List<String> = listOf("outerWave", "**", "Fill 1"),
    val innerKeyPath: List<String> = listOf("innerWave", "**", "Fill 1"),
)
