package com.daedan.festabook.data.util

import kotlinx.coroutines.withTimeoutOrNull

suspend fun <T> withTimeoutOrNullFallback(
    timeMillis: Long = 2000L,
    onFallback: () -> Unit = {},
    producer: suspend () -> T?,
): T? =
    withTimeoutOrNull(
        timeMillis = timeMillis,
    ) {
        producer()
    } ?: run {
        onFallback()
        null
    }
