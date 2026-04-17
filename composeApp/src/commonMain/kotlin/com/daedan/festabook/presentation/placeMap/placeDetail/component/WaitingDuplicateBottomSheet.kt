package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.waiting_duplicate_discription
import festabookkmp.composeapp.generated.resources.waiting_duplicate_my_waiting
import festabookkmp.composeapp.generated.resources.waiting_duplicate_register_new
import festabookkmp.composeapp.generated.resources.waiting_duplicate_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitingDuplicateBottomSheet(
    onMyWaitingClick: () -> Unit,
    onRegisterNewClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FestabookColor.white,
        modifier = modifier,
        dragHandle = {},
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = festabookSpacing.paddingScreenGutter)
                    .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            Text(
                text = stringResource(Res.string.waiting_duplicate_title),
                style = FestabookTypography.displaySmall,
                color = FestabookColor.black,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(Res.string.waiting_duplicate_discription),
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = festabookSpacing.paddingBody2),
            )

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
            ) {
                DuplicateBottomSheetButton(
                    text = stringResource(Res.string.waiting_duplicate_my_waiting),
                    isOutlined = true,
                    onClick = onMyWaitingClick,
                    modifier = Modifier.weight(1f),
                )

                DuplicateBottomSheetButton(
                    text = stringResource(Res.string.waiting_duplicate_register_new),
                    isOutlined = false,
                    onClick = onRegisterNewClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DuplicateBottomSheetButton(
    text: String,
    isOutlined: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(festabookShapes.radius2)
                .then(
                    if (isOutlined) {
                        Modifier
                            .border(
                                width = 1.dp,
                                color = FestabookColor.gray300,
                                shape = festabookShapes.radius2,
                            ).background(FestabookColor.white)
                    } else {
                        Modifier.background(FestabookColor.black)
                    },
                ).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = festabookSpacing.paddingBody4),
            text = text,
            style = FestabookTypography.titleSmall,
            color = if (isOutlined) FestabookColor.black else FestabookColor.white,
        )
    }
}
