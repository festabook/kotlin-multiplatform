package com.daedan.festabook.presentation.error

import android.app.Application
import android.os.Process
import kotlin.system.exitProcess

class FestabookGlobalExceptionHandler(
    private val application: Application,
) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(
        t: Thread,
        e: Throwable,
    ) {
        application.startActivity(ErrorActivity.newIntent(application, e))
        Process.killProcess(Process.myPid())
        exitProcess(-1)
    }
}
