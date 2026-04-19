package com.daedan.festabook.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FestabookTopAppBar(
    title: String,
    modifier: Modifier = Modifier.background(FestabookColor.white),
    navigationIcon: @Composable () -> Unit = {},
    style: TextStyle = FestabookTypography.displayLarge,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    top = festabookSpacing.paddingTitleHorizontal,
                    bottom = festabookSpacing.paddingBody4,
                    start = festabookSpacing.paddingScreenGutter,
                    end = festabookSpacing.paddingScreenGutter,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcon()
        Text(text = title, style = style)
    }
}

@Composable
@Preview(showBackground = true)
private fun FestabookTopAppBarPreview() {
    FestabookTopAppBar(title = "FestaBook" , navigationIcon = { IconButton(onClick = {  }) {
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_back),
            contentDescription = null,
        )
    }})
}
