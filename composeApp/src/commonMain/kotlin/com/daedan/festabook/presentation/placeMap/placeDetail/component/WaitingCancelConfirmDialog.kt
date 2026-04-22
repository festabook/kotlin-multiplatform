package com.daedan.festabook.presentation.placeMap.placeDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.waiting_cancel_confirm_dismiss
import festabookkmp.composeapp.generated.resources.waiting_cancel_confirm_submit
import org.jetbrains.compose.resources.stringResource

@Composable
fun WaitingCancelConfirmDialog(
    title: String,
    onDismissClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(festabookShapes.radius4)
                    .background(FestabookColor.white)
                    .padding(festabookSpacing.paddingScreenGutter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = FestabookTypography.displaySmall,
                color = FestabookColor.black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
            ) {
                CancelConfirmDialogButton(
                    text = stringResource(Res.string.waiting_cancel_confirm_dismiss),
                    isOutlined = true,
                    onClick = onDismissClick,
                    modifier = Modifier.weight(1f),
                )

                CancelConfirmDialogButton(
                    text = stringResource(Res.string.waiting_cancel_confirm_submit),
                    isOutlined = false,
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CancelConfirmDialogButton(
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
