package com.daedan.festabook.presentation.error

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.daedan.festabook.logging.FirebaseCrashlytics
import com.daedan.festabook.presentation.theme.FestabookTheme
import kotlin.system.exitProcess

class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.setDefaultUncaughtExceptionHandler(null)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        getThrowableFromIntent()?.let { FirebaseCrashlytics.recordException(it) }
        setContent {
            FestabookTheme {
                ErrorScreen(onRestart = ::restartApplication)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getThrowableFromIntent(): Throwable? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(KEY_ERROR, Throwable::class.java)
        } else {
            intent.getSerializableExtra(KEY_ERROR) as? Throwable
        }

    private fun restartApplication() {
        val intentForPackage = packageManager.getLaunchIntentForPackage(packageName)
        if (intentForPackage == null) {
            finishAffinity()
            return
        }
        val mainIntent = Intent.makeRestartActivityTask(intentForPackage.component)
        startActivity(mainIntent)
        exitProcess(0)
    }

    companion object {
        private const val KEY_ERROR = "error"

        fun newIntent(
            context: Context,
            error: Throwable,
        ): Intent =
            Intent(context, ErrorActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra(KEY_ERROR, error)
            }
    }
}
