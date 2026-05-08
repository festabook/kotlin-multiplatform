package com.daedan.festabook.presentation.placeMap.component

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.daedan.festabook.presentation.placeMap.intent.state.MapDelegate
import com.daedan.festabook.presentation.placeMap.platform.NaverMap
import com.naver.maps.map.MapView
import kotlinx.coroutines.suspendCancellableCoroutine

@Composable
actual fun NaverMapContent(
    modifier: Modifier,
    mapDelegate: MapDelegate,
    onMapDrag: () -> Unit,
    onMapReady: (NaverMap) -> Unit,
    isVisible: Boolean,
    content: @Composable (NaverMap?) -> Unit,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val currentOnMapReady by rememberUpdatedState(onMapReady)

    LaunchedEffect(mapView) {
        val naverMap =
            suspendCancellableCoroutine { cont ->
                mapView.getMapAsync { platformMap ->
                    cont.resumeWith(Result.success(NaverMap(platformMap)))
                }
            }
        currentOnMapReady(naverMap)
        mapDelegate.initMap(naverMap)
    }
    Box(modifier = modifier) {
        // TODO AndroidView와 CMP 뷰의 혼용으로 컴파일러 경고 발생중 -> 추후 해결하겠습니다
        AndroidView(
            factory = { mapView },
            modifier = Modifier.dragInterceptor(onMapDrag),
            onRelease = {
                mapView.onDestroy()
            },
        )
        content(mapDelegate.value)
    }
    RegisterMapLifeCycle(mapView)
}

@Composable
private fun RegisterMapLifeCycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val savedInstanceState = rememberSaveable { Bundle() }

    DisposableEffect(lifecycle, mapView) {
        val mapLifecycleObserver =
            mapView.lifecycleObserver(
                savedInstanceState.takeUnless { it.isEmpty },
                previousState,
            )

        val callbacks =
            object : ComponentCallbacks2 {
                override fun onConfigurationChanged(config: Configuration) = Unit

                @Deprecated("This callback is superseded by onTrimMemory")
                override fun onLowMemory() {
                    mapView.onLowMemory()
                }

                override fun onTrimMemory(level: Int) {
                    mapView.onLowMemory()
                }
            }

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            mapView.onSaveInstanceState(savedInstanceState)
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun MapView.lifecycleObserver(
    savedInstanceState: Bundle?,
    previousState: MutableState<Lifecycle.Event>,
): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> this.onCreate(savedInstanceState)
            Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDestroy()
            else -> throw IllegalStateException()
        }
        previousState.value = event
    }
