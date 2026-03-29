package com.daedan.festabook.presentation.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_uncaught_error
import festabookkmp.composeapp.generated.resources.uncaught_exception_confirm
import festabookkmp.composeapp.generated.resources.uncaught_exception_message
import festabookkmp.composeapp.generated.resources.uncaught_exception_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = festabookSpacing.paddingScreenGutter),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_uncaught_error),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = FestabookColor.gray400,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody5))
        Text(
            text = stringResource(Res.string.uncaught_exception_title),
            style = FestabookTypography.displaySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody2))
        Text(
            text = stringResource(Res.string.uncaught_exception_message),
            style = FestabookTypography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody5))
        Button(
            onClick = onRestart,
            colors =
                ButtonColors(
                    containerColor = FestabookColor.black,
                    contentColor = FestabookColor.white,
                    disabledContainerColor = FestabookColor.gray400,
                    disabledContentColor = FestabookColor.white,
                ),
        ) {
            Text(
                text = stringResource(Res.string.uncaught_exception_confirm),
                style = FestabookTypography.bodyMedium,
            )
        }
    }
}
