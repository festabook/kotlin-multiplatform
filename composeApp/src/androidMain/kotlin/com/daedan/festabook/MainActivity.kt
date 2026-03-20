package com.daedan.festabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.FestabookScreen
import com.daedan.festabook.presentation.theme.FestabookTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    private val metroVmf by lazy {
        (application as FestabookApp).festabookAppGraph.metroViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            FestabookTheme {
                FestabookScreen(
                    onAppFinish = ::finish,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    FestabookTheme {}
}
