package com.daedan.festabook.presentation.placeMap.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.dragInterceptor(onMapDrag: () -> Unit): Modifier =
    this.then(
        Modifier.pointerInput(Unit) {
            val touchSlop = viewConfiguration.touchSlop
            awaitPointerEventScope {
                while (true) {
                    val downEvent = awaitPointerEvent(pass = PointerEventPass.Initial)
                    val downChange = downEvent.changes.firstOrNull { it.pressed } ?: continue

                    val startPosition = downChange.position
                    var isDragEmitted = false

                    do {
                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        val change = event.changes.firstOrNull { it.id == downChange.id }

                        if (change != null && change.pressed) {
                            val currentPosition = change.position
                            val distance = (currentPosition - startPosition).getDistance()

                            if (!isDragEmitted && distance > touchSlop) {
                                onMapDrag()
                                isDragEmitted = true
                            }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
        },
    )
