package com.daedan.festabook

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.presentation.schedule.component.ScheduleScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier =
//                modifier
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .safeContentPadding()
//                    .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
        ScheduleScreen(modifier = modifier)
    }
}
