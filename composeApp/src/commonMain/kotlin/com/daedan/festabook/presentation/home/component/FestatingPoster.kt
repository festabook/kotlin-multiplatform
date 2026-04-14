package com.daedan.festabook.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.festa_ting_poster
import festabookkmp.composeapp.generated.resources.home_festating_poster_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FestatingPoster(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.festa_ting_poster),
        contentDescription = stringResource(Res.string.home_festating_poster_image),
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
@Preview
private fun FestatingItemPreview() {
    FestatingPoster()
}
