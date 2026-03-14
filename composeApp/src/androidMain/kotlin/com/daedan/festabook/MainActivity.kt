package com.daedan.festabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daedan.festabook.presentation.placeMap.component.PlaceMapRoute
import com.daedan.festabook.presentation.platform.rememberLocationSource
import com.daedan.festabook.presentation.theme.FestabookTheme
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
                CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                    PlaceMapRoute(
                        placeMapViewModel = viewModel(factory = metroVmf),
                        onStartPlaceDetail = {},
                        onShowErrorSnackBar = {},
                        locationSource = rememberLocationSource(),
                    )
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
