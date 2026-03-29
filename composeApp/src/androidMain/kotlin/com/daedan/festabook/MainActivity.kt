package com.daedan.festabook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.FestabookScreen
import com.daedan.festabook.presentation.theme.FestabookTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FestabookTheme {
                FestabookScreen(
                    onAppFinish = { finish() },
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    FestabookTheme {}
}
