package com.daedan.festabook.presentation.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.content_description_iv_fail_load
import festabookkmp.composeapp.generated.resources.error_fail_to_load_info
import festabookkmp.composeapp.generated.resources.ic_fail_load
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorStateScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_fail_load),
            contentDescription = stringResource(Res.string.content_description_iv_fail_load),
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(festabookSpacing.paddingBody2))
        Text(
            text = stringResource(Res.string.error_fail_to_load_info),
            style = FestabookTypography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStateScreenPreview() {
    ErrorStateScreen()
}
