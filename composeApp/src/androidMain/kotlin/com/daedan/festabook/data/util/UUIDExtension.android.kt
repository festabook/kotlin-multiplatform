package com.daedan.festabook.data.util

import java.util.UUID

actual fun randomUUID(): String = UUID.randomUUID().toString()
