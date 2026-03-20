package com.daedan.festabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.imageLoader
import com.daedan.festabook.presentation.explore.component.ExploreScreen
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.skydoves.landscapist.coil3.LocalCoilImageLoader
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
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
                CompositionLocalProvider(
                    LocalMetroViewModelFactory provides metroVmf,
                    LocalCoilImageLoader provides imageLoader,
                ) {
                    Scaffold { innerPadding ->
                        ExploreScreen(
                            onNavigateToMain = {},
                            onBackClick = {},
                            viewModel = viewModel(factory = metroVmf),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    FestabookTheme {}
}
