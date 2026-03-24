package com.daedan.festabook.data.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID.UUID().UUIDString
