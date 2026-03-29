package com.daedan.festabook.presentation.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookSpacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FestabookTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = FestabookTypography.displayLarge,
) {
    Text(
        text = title,
        style = style,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    top = festabookSpacing.paddingTitleHorizontal,
                    bottom = festabookSpacing.paddingBody4,
                    start = festabookSpacing.paddingScreenGutter,
                    end = festabookSpacing.paddingScreenGutter,
                ),
    )
}

@Composable
@Preview(showBackground = true)
private fun FestabookTopAppBarPreview() {
    FestabookTopAppBar(title = "FestaBook")
}
